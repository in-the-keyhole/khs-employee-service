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
}
