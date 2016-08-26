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
   sh 'wget https://cloud.docker.com/api/app/v1/service/ceb5eaec-9b42-4a38-b744-72a15bf5623b/trigger/71ed527c-c1d9-4964-b6d2-e6c11d2e199f/call/ --post-data ""'
   input 'Does Staging look okay?'
   
 stage 'Deploy to Production'
   echo 'Deployed to Production'
}
