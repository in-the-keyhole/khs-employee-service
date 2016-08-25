node('docker') {
  stage 'Pull from SCM'  
      //Passing the pipeline the ID of my GitHub credentials and specifying the repo for my app
      git credentialsId: 'github-credentials', url: 'https://github.com/in-the-keyhole/khs-employee-service.git'
  
//   stage 'Test Code'  
//   sh 'mvn install'

 stage 'Build app' 
    //Running the maven build and archiving the war
    sh 'mvn package'
  
 stage 'Build Docker Image'
   //Packaging the image into a Docker image
   def pkg = docker.build ('keyholesoftware/reference-employee-service', '.')

 stage 'Push Image to DockerHub'
   //Pushing the packaged app in image into DockerHub
   docker.withRegistry ('https://index.docker.io/v1/', 'github-credentials') {
       sh 'ls -lart' 
       pkg.push 'latest'
   }
   
 stage 'Deploy to Staging'
   //Deploy to Staging on Docker cloud
   sh 'wget https://cloud.docker.com/api/app/v1/service/ceb5eaec-9b42-4a38-b744-72a15bf5623b/trigger/71ed527c-c1d9-4964-b6d2-e6c11d2e199f/call/ --post-data ""'
}
