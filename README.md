# Shake The Lake Backend

## Quick Start

 * clone the repository `git clone git@gitlab.fhnw.ch:ip34-24bb/ip34-24bb_shakethelake/shakethelakebackend.git`
 * `cd shakethelakebackend`
 * `mvn clean install package`
 * `docker-compose -f docker-compose.yml up`
 * open your browser and navigate to `http://localhost:8080/`

## IDE's
The project can be used in IntelliJ or vscode.

### IntelliJ
 * Open the project in IntelliJ
 * Import the maven project
 * Run the `ShakeTheLakeBackend` compose configuration

### vscode
  * Open the project in vscode
  * Install the `Dev Containers` extension
  * Open the project in a container
  * Run the `ShakeTheLakeBackendApplication` class

#### DevContainer

for changing the dev container settings, you can edit the `.devcontainer/devcontainer.json` file and the assosited `.devcontainer/docker-compose.dev.yml` file. On opening the dev container vscoded will write the hole compose configuration in the `docker-compose.rendered.yml` file. this file should not be edited manually and is needed due to a bug in vscode [issue 8734](https://github.com/microsoft/vscode-remote-release/issues/8734).
