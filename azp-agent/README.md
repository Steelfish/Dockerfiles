# azp-agent

Dockerfile for an Azure Pipelines Agent.

The agent runs with:
* Docker 18.09.7
* Kubectl v1.3.5
* .NET 3.1 SDK
* Cake .NET global tool

Available environment variables:

* AZP_URL   | The url for the Azure DevOps instance to connect to.
* AZP_TOKEN | The PAT token for the Agent.
* AZP_AGENT | The name of the Agent.
* AZP_POOL | The name of the agent pool to join.
* AZP_WORK | The Agent's work directory. 


Be sure to the AZP_WORK directory to a mounted path on the docker host to ensure docker volume mounts are correct when using the Agent's Docker capabilities.


Sample command:

`docker run -e AZP_URL=<Azure DevOps instance> -e AZP_TOKEN=<PAT token> -e AZP_AGENT_NAME=mydockeragent dockeragent:latest`
