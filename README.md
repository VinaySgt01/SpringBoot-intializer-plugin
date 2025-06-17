# 🌱 Spring Initializr IntelliJ Plugin

An IntelliJ plugin that embeds the official [start.spring.io](https://start.spring.io) website into the **New Project Wizard**, allowing you to generate Spring Boot projects **without leaving the IDE**.

---

## 🚀 Why This Plugin?

Creating a new Spring Boot project often means:

1. Opening a browser.
2. Going to [start.spring.io](https://start.spring.io).
3. Downloading a ZIP.
4. Extracting it manually.
5. Importing it back into IntelliJ.

😩 **Too many steps.**  
🔌 **This plugin solves that.**

---

## ✨ Features

- ✅ Embedded Spring Initializr site in the IntelliJ UI.
- ✅ Automatic download of the generated ZIP file.
- ✅ Automatic extraction and project setup.
- ✅ Progress indicators and helpful messages.
- ✅ No need to switch context or leave the IDE.

---

## 📦 Installation

### 🔁 From GitHub

1. Download the latest plugin release from the [Releases page](https://github.com/VinaySgt01/SpringBoot-intializer-plugin/blob/main/springinitializr-1.0.0.zip).
   > (File will be a `.zip`, `.jar`, or `.jip`)
2. Open IntelliJ IDEA.
3. Go to **Settings → Plugins**.
4. Click the ⚙️ gear icon → **Install Plugin from Disk**.
5. Select the downloaded plugin file.
6. Restart the IDE.

---
## 🧪 How to Use

1. Open IntelliJ IDEA.
2. Click **New Project** → Select **Spring Initializr** (added by this plugin).
3. The embedded Spring Initializr UI will load inside IntelliJ.
4. Configure your project (Group, Artifact, Dependencies, etc).
5. Click **Generate**.
6. The plugin will automatically:
   - Download the project ZIP
   - Extract it to a temporary location
   - Set up the IntelliJ module with the generated code
7. Click **Next** to finish project setup.

### 🖼 Preview

Here’s how the embedded Spring Initializr looks inside IntelliJ:

![Spring Initializr Plugin Preview](https://raw.githubusercontent.com/VinaySgt01/SpringBoot-intializer-plugin/main/Image%20(2).png)


---

## 📁 File Structure

This plugin includes the following key files:

- `StartSpringIOModuleWizardStep.java`: Handles the embedded browser and download logic.
- `ZipUtils.java`: Extracts downloaded Spring ZIPs.
- `StartSpringIOModuleBuilder.java`: Sets up the IntelliJ module after download.
- `StartSpringIOModuleType.java`: Registers the module type in IntelliJ.
- `SpringInitializrIcons.java`: Icon support for the wizard UI.

---

## 🛠 Requirements

- IntelliJ IDEA 2021.3 or later.
- JDK 11+.
- Internet connection (to load [start.spring.io](https://start.spring.io)).

---

## 📣 Why I Built This

As a developer, I found it annoying to leave IntelliJ just to create a new Spring Boot project.  
This plugin eliminates that friction by integrating the official Spring Initializr **directly** into the New Project Wizard.

---

## 🧰 Tech Stack

- Java
- IntelliJ Platform SDK
- JetBrains JCEF (Chromium Embedded Framework)
- Apache Commons Compress (for ZIP handling)

---

## 🤝 Contributing

Pull requests are welcome! Feel free to fork and improve.

---

## 📝 License

This project is licensed under the MIT License.

---

## 🔗 Useful Links

- [Spring Initializr](https://start.spring.io)
- [IntelliJ Platform SDK Docs](https://plugins.jetbrains.com/docs/intellij/welcome.html)

---

## 🙌 Acknowledgements

Thanks to the JetBrains platform for their extensibility and to the Spring team for providing such a great project generator.

