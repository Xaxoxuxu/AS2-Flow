
<h1 align="center">AS2 Flow</h1>

⚠️ *Project still in development. By no means you should be relying on the product for a real B2B file transfer solution (for now)!*

The goal of this project is to integrate the AS2 protocol rules into a modern Vaadin Spring Boot based application. The end product should be a hardened and secure enterprise-level file transfer/management solution.

The project uses the AS2 core implementation of https://github.com/phax/as2-lib and builds on top of it with minor changes to the workflow.

## The AS2 Protocol
AS2 is a transport protocol specified in [RFC 4130](http://www.ietf.org/rfc/rfc4130.txt). AS2 version 1.1 adding compression is specified in [RFC 5402](http://www.ietf.org/rfc/rfc5402.txt). The MDN is specified in [RFC 3798](http://www.ietf.org/rfc/rfc3798.txt). Algorithm names are defined in [RFC 5751](https://www.ietf.org/rfc/rfc5751.txt) (S/MIME 3.2) which supersedes [RFC 3851](https://www.ietf.org/rfc/rfc3851.txt) (S/MIME 3.1);
*(Links take some time to load, but they do load eventually)*

## Running and debugging the application

### Running the application from the command line.
To run from the command line, use `mvn` and open http://localhost:8080 in your browser.

### Running and debugging the application in Intellij IDEA
- Locate the Application.java class in the Project view. It is in the src folder, under the main package's root.
- Right-click on the Application class
- Select "Debug 'Application.main()'" from the list

After the application has started, you can view it at http://localhost:8080/ in your browser. 
You can now also attach breakpoints in code for debugging purposes, by clicking next to a line number in any source file.

### Running and debugging the application in Eclipse
- Locate the Application.java class in the Package Explorer. It is in `src/main/java/com/as2flow`.
- Right-click on the file and select `Debug As` --> `Java Application`.

Do not worry if the debugger breaks at a `SilentExitException`. This is a Spring Boot feature and happens on every startup.

After the application has started, you can view it at http://localhost:8080/ in your browser.
You can now also attach breakpoints in code for debugging purposes, by clicking next to a line number in any source file.
## Project structure

- `MainView.java` in `src/main/java/com/as2flow/views` contains the navigation setup. It uses [App Layout](https://vaadin.com/components/vaadin-app-layout).
- `views` package in `src/main/java/com/as2flow` contains the server-side Java views of your application.
- `views` folder in `frontend/` contains the client-side JavaScript views of your application.
- `entity | repository | service` folders in `src/main/java/com/as2flow/backend/` contain the persistence layer logic for the application.
- `security` folder in `src/main/java/com/as2flow/` contains the Spring security configuration for the app.
- `as2` folder in `src/main/java/com/as2flow/backend/` contains the AS2 related backend logic for the protocol.  `src/main/java/com/as2flow/As2ServletConfig`  contains the AS2 servlet config.
- package `src/test/java/com/as2flow` contains the tests for the application.
- package `src/main/java/webapp` contains the files for the offline PWA page.

## What is left?
- Asynchronous MDN is not implemented
- Monitoring
- Flexible partnership configurations
- ...
