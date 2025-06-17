package vk;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.externalSystem.model.ProjectSystemId;
import com.intellij.openapi.externalSystem.service.execution.ProgressExecutionMode;
import com.intellij.openapi.externalSystem.util.ExternalSystemUtil;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.compress.archivers.ArchiveException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class StartSpringIOModuleBuilder extends ModuleBuilder {

    public static final Key<String> START_SPRING_IO_DOWNLOADED_ZIP_LOCATION =
            Key.create("start.spring.io.downloaded.zip.path");

    private WizardContext context;
    private String projectName;
    private String contentRootPath;

    @Override
    public ModuleType<?> getModuleType() {
        return StartSpringIOModuleType.getInstance();
    }

    @Override
    public @Nullable ModuleWizardStep getCustomOptionsStep(WizardContext context, Disposable parentDisposable) {
        this.context = context;
        return new StartSpringIOModuleWizardStep(this, context, parentDisposable);
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public @Nullable ModuleWizardStep modifySettingsStep(@NotNull SettingsStep settingsStep) {
        if (projectName != null) {
            Objects.requireNonNull(settingsStep.getModuleNameLocationSettings()).setModuleName(projectName);
        }
        return super.modifySettingsStep(settingsStep);
    }

    @Override
    public @Nullable Project createProject(String name, String path) {
        try {
            String downloadedZipPath = context.getUserData(START_SPRING_IO_DOWNLOADED_ZIP_LOCATION);
            System.out.println("📦 ZIP file path: " + downloadedZipPath);

            if (downloadedZipPath == null || downloadedZipPath.isBlank()) {
                ApplicationManager.getApplication().invokeLater(() ->
                        Messages.showErrorDialog("ZIP download path is not set.", "Error")
                );
                throw new IllegalStateException("ZIP path is null or empty.");
            }

            File zipFile = new File(downloadedZipPath);
            if (!zipFile.exists()) {
                ApplicationManager.getApplication().invokeLater(() ->
                        Messages.showErrorDialog("ZIP file does not exist at: " + downloadedZipPath, "Error")
                );
                throw new IllegalStateException("ZIP file not found at: " + downloadedZipPath);
            }

            String extractTo = Path.of(path).toAbsolutePath().toString();
            System.out.println("📂 Extracting to: " + extractTo);

            ZipUtils.extractZip(downloadedZipPath, extractTo);

            File extractDir = new File(extractTo);
            File[] files = extractDir.listFiles(File::isDirectory);

            if (files != null && files.length == 1) {
                File nestedProjectDir = files[0];
                System.out.println("📁 Found nested directory: " + nestedProjectDir.getAbsolutePath());
                extractTo = nestedProjectDir.getAbsolutePath();
            }

            this.contentRootPath = extractTo;

            File pomFile = new File(extractTo, "pom.xml");
            File gradleFile = new File(extractTo, "build.gradle");
            File gradleKtsFile = new File(extractTo, "build.gradle.kts");

            boolean isMaven = pomFile.exists();
            boolean isGradle = gradleFile.exists() || gradleKtsFile.exists();

            System.out.println("🔍 Detected build files → Maven: " + isMaven + ", Gradle: " + isGradle);

            VirtualFile extractedDir = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(new File(extractTo));
            if (extractedDir != null) {
                extractedDir.refresh(false, true);
            } else {
                System.out.println("⚠️ Could not refresh VirtualFile for: " + extractTo);
            }

            setModuleFilePath(Path.of(contentRootPath, projectName + ".iml").toString());

            Project project = super.createProject(name, extractTo);
            if (project != null && project.getBasePath() != null) {
                System.out.println("🚀 Project base path: " + project.getBasePath());

                if (isGradle) {
                    System.out.println("🔄 Importing as Gradle project...");
                    ExternalSystemUtil.refreshProject(
                            project,
                            new ProjectSystemId("GRADLE"),
                            project.getBasePath(),
                            false,
                            ProgressExecutionMode.MODAL_SYNC
                    );
                } else if (isMaven) {
                    System.out.println("📦 Maven project detected. Please manually import it.");
                    ApplicationManager.getApplication().invokeLater(() ->
                            Messages.showInfoMessage(
                                    "Maven project detected.\nPlease use 'File > Open' or 'Add Framework Support' to import it manually.",
                                    "Manual Maven Import Required"
                            )
                    );
                } else {
                    System.out.println("⚠️ No recognized build file found.");
                }
            }

            return project;

        } catch (IOException | ArchiveException e) {
            ApplicationManager.getApplication().invokeLater(() ->
                    Messages.showErrorDialog("Failed to unzip Spring project: " + e.getMessage(), "Error")
            );
            throw new RuntimeException("Error creating Spring Boot project", e);
        }
    }


    public List<VirtualFile> getModuleRoots() {
        if (contentRootPath == null) return List.of();
        File rootDir = new File(contentRootPath);
        VirtualFile vf = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(rootDir);
        return vf != null ? List.of(vf) : List.of();
    }
}
