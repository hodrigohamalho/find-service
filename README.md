# Evacuee find-service

Quarkus based backend service for the [Find My Relative demo application](https://github.com/Emergency-Response-Demo/findmyrelative-frontend).



## Running and Test

The *find-service* is installed by default as a KNative Serving service in an ER-Demo installation .

You can invoke the *find-service* of the ER-Demo by executing the following:

1. Use the simulators of ER-Demo to create 1 or more *incidents*.
2. Use an http client to invoke the _find-service_.  Sample queries using the _curl_ utility are as follows:


   1. If you haven't done so already, set the value of the *OCP_USERNAME* environment variable in your shell:
     ```
     export OCP_USERNAME=user1
     ```

   2. Given a evacuee's name, identify the details and status of the corresponding *incident*:
     ```
     $ export victimName='Eli%20Hill'    # changeme using URL encoded space

     $ curl $(echo -en "\n$(oc get kservice $OCP_USERNAME-find-service -n $OCP_USERNAME-er-demo --template='{{ .status.url }}')/find/victim/byName/$victimName\n\n") | jq .


     {
       "victims": [
        {
          "id": "921d8087-9d0c-4193-888b-2f36f713f544",
          "lat": "34.236220397748056",
          "lon": "-77.85671833586497",
          "medicalNeeded": true,
          "numberOfPeople": 3,
          "victimName": "Eli Hill",
          "victimPhoneNumber": "(828) 555-3028",
          "timeStamp": 1598920936720,
          "status": "PICKEDUP"
        }
        ]
      }

     ```
   3. List the shelter details where evacuees associated with an incidentId were dropped off:
     ```
     $ incidentId=changeme

     $ curl $(echo -en "\n$(oc get kservice $OCP_USERNAME-find-service -n $OCP_USERNAME-er-demo --template='{{ .status.url }}')/find/shelter/$incidentId\n\n") | jq .


      {
       "status": true,
       "shelter": {
        "name": "Port City Marina",
        "lat": "34.2461",
        "lon": "-77.9519"
       }
      }
    ```
   
## Old Docs

- Locally

    Modify the `INCIDENT_SERVICE_URL` and `MISSION_SERVICE_URL` in `application.properties` file.

    Run `./mvnw compile quarkus:dev`
  
- Container Image

1. Build the container image:
   
    ```
    mvn clean package -DskipTests
    buildah bud -f src/main/docker/Dockerfile.jvm -t quay.io/emergencyresponsedemo/find-service:0.0.4 .
    buildah bud -f src/main/docker/Dockerfile.jvm -t $INTERNAL_OCP_REGISTRY_HOST/user1-er-demo/user1-find-service:0.0.4 .
    ```

2. Push to registries
   ```
   oc create is user1-find-service -n user1-er-demo && \
       podman push $HOST/user1-er-demo/user1-find-service:0.0.4 --tls-verify=false          : push to internal OCP registry 

   podman push quay.io/emergencyresponsedemo/find-service:0.0.4 .                           # push to quay
   ```


  ```
  curl user1-find-service:8080/find/shelter/23
  ```

  
## Deploying using Tekton Pipeline

* Prerequisite

    - OpenShift v4.x Cluster with cluster admin permissions.
     
    - Tekton Pipeline Installed. You can install using `OpenShift Pipelines Operator` from the Operator Hub.
    
    - Knative Installed. You can follow this [steps](https://docs.openshift.com/container-platform/4.2/serverless/installing-openshift-serverless.html) to install Serverless in OpenShift. 
    
    - Tekton CLI (tkn) (Optional). Download the Tekton CLI by following [instructions](https://github.com/tektoncd/cli#installing-tkn) available on the CLI GitHub repository.

* Installation Steps

    1. Fork this repository so that you can create a trigger for any GitHub Event which will trigger the pipeline and deploy the new code.
    
    2. Clone the repository to your local machine.
    
       ```
       git clone https://github.com/<your-github-username>/find-service
       ```
        
    3. Login to your OpenShift Cluster.
    
    4. Create a new project `find-my-relative`
    
       ``` 
       oc new-project find-my-relative
       ```
         
    5. Before Installing Pipeline, Create a Config Map with name ` erd-urls ` which will have `INCIDENT_SERVICE_URL` and `MISSION_SERVICE_URL`. 
    
       To create config map, you can use `erd-urls-config-map.yaml` in `k8s` folder. Add the urls and apply the yaml file using below command.
    
       ```
       oc apply -f k8s/erd-urls-config-map.yaml
       ```
    
    6. Traverse to the pipeline folder.
        
       ```
       cd pipeline/
       ```   
    
    7. Install the Pipeline Resources,Task and Pipeline.
    
        1. Pipeline - There are two task in pipeline - buildah and openshift-client.
        
            Install pipeline using below command - 
        
            ```
            02-pipelines/01-findmyrelative-backend-pipeline.yaml
            ```
         
        2. Tasks - 
            
            - buildah - This task build a image using the Dockerfile and then push it to repository which we will specify in pipeline resource.
            
            - openshift-client - Here, this task is used to apply knative service which will deploy the image as serverless.
            
            buildah and openshift-client used from the cluster task. So, they come pre-installed with the OpenShift Pipelines Operator.
            
        3. Pipeline Resources - First is git resource where we give git url of repository and second is image resource where the image will be pushed.
        
            Here we are using OpenShift Internal Registry but you can also use any external registry like DockerHub, Quay. 
            
            Before Installing Resources, Replace your git url of find-service in `01-findmyrelative-backend-git-resource.yaml`
            
            Now, you can install using below command - 
            
            ```
            oc apply -f 01-pipelineresources/
            ``` 
        
        Now, we are ready to run the pipeline. You can run it by using below command or Go to OpenShift Web Console -> find-my-relative Project -> Pipeline Tab -> Pipeline -> Click on pipeline and Start.
        
        ``` 
        tkn pipeline start findmyrelative-backend-pipeline -r source-git-repo=findmyrelative-backend-git-repo -r image-resource-name=findmyrelative-backend-image -s pipeline
        ```  
        
        Also, You can start pipeline using pipeline run.      
        
        ```
        oc apply -f 03-pipelineruns/01-findmyrelative-backend-pipelinerun.yaml
        ```
              
    8. Next Step is to create a trigger so that on any code change in GitHub the pipeline will start and deploy the new code. 
         
        Install Event Listener, Trigger Template and Trigger binding.
        
        ```
        oc apply -f 04-pipeline-triggers/
        ```
        
        New pod will be created for Event listener. Get the url for Event Listener which we will need for creating Webhook - ` oc get route`.
    
    9. Create a webhook  -
    
        Firstly, create a GitHub Personal access token. Follow this [instructions](https://help.github.com/en/github/authenticating-to-github/creating-a-personal-access-token-for-the-command-line#creating-a-token) to create a GitHub token.
        
        The token should have the access - `public_repo`  and `admin:repo_hook`.  
        
        To create a webhook - Go to GitHub Repository -> Settings -> Webhooks -> Add webhook -> Add the EventListener url, the token as secret and select the event.
        
        Also, you can create a webhook using Webhook task which you can find in `03_create-webhook-task.yaml`. Follow below steps to create using a task - 
        
          1. Add your GitHub Personal access token and Random String data in `secret` in `02_webhook-secret.yaml`.
       
          2. In `04_backend-webhook-run.yaml` add your GitHub Username for `GitHubOrg` and `GitHubUser`. Add the Event Listener's url for `ExternalDomain`.
        
        Now, install the task and the task run.
        
        ```
        oc apply -f 05-github-webhooks/
        ```
        
        If you go to Github, you can see a webhook created for the repository.   
        
    10. Now, when you change code and push it to repository. You can see a new pipelinerun is started.              
        
