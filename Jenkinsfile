node('docker') {
  stage 'Pull from Git'  
    git credentialsId: 'github-credentials', url: 'https://github.com/in-the-keyhole/khs-employee-service.git'
  
  stage 'Perform Tests'  
    sh 'mvn test'

 stage 'Package Application' 
    sh 'mvn package -DskipTests=true'
  
 stage 'Build Docker Image'
   def pkg = docker.build ('keyholesoftware/reference-employee-service', '.')

 stage 'Push Image to Repository'
   docker.withRegistry ('https://index.docker.io/v1/', 'github-credentials') {
       sh 'ls -lart' 
       pkg.push 'latest'
   }
   
 stage 'Deploy to Staging'
   //Deploy to Staging on Docker cloud
   sh 'wget https://cloud.docker.com/api/app/v1/service/9659bdc5-49da-469f-9435-f1bc9e946a2e/trigger/9f1c0add-a0fe-4541-b68a-4ac1ada8e622/call/ --post-data ""'
   input 'Does Staging look okay?'
   
 stage 'Deploy to Production'
   echo 'Deployed to Production'
}
