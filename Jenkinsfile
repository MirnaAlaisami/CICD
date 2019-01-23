
node{
  
  //Define all variables
  def appName = 'todobackend'
  def serviceName = "${appName}-backend" 
  def imageTag = "mirna/${appName}:${env.BUILD_NUMBER}"
  
  //Checkout Code from Git
  checkout scm
  
  //Stage 1 : Build the docker image.
	
  stage('Build image') {
	  container('docker'){
                    // This is where we build the Docker image
                
      sh("docker build -f Dockerfile-todobackend -t mirna/todobackend:${env.BUILD_NUMBER} .")
		  }
  }
	
  
  //Stage 2 : Push the image to docker registry
  stage('Push image to registry') {
	  container('docker'){
	withCredentials([[$class: 'UsernamePasswordMultiBinding',
  credentialsId: 'dockerhub',
  usernameVariable: 'USERNAME',
  passwordVariable: 'PASSWORD']]) {

		docker.withRegistry('', 'dockerhub') {
		sh "docker login -u ${USERNAME} -p ${PASSWORD}"
		sh("docker push mirna/todobackend:${env.BUILD_NUMBER}")
			    

		  }
		  
	  }
	  }
  }
	
	
//Stage 3 : Deploy Application
  stage('Deploy Application on K8s') {
	  container('kubectl'){
		  withKubeConfig([credentialsId: 'be4d8aa0-a081-466c-a8e9-dc0c151b3654',
                    caCertificate: 'LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSURERENDQWZTZ0F3SUJBZ0lSQUlkT1FCY3RPcGcvT2VnSzZFbmJiM1F3RFFZSktvWklodmNOQVFFTEJRQXcKTHpFdE1Dc0dBMVVFQXhNa01UWTJOekE0TVdNdE1USXdZaTAwWWpNMExUZ3laRFl0TldJMU1XRXhOakJtWWprdwpNQjRYRFRFNU1ERXhPREE0TVRVMU9Wb1hEVEkwTURFeE56QTVNVFUxT1Zvd0x6RXRNQ3NHQTFVRUF4TWtNVFkyCk56QTRNV010TVRJd1lpMDBZak0wTFRneVpEWXROV0kxTVdFeE5qQm1Zamt3TUlJQklqQU5CZ2txaGtpRzl3MEIKQVFFRkFBT0NBUThBTUlJQkNnS0NBUUVBblJ6SDFsMzVmeUdpV2N3UjIwRXFuTDQ3bmVEcEpua1k4ejhLRDdFVwpPT2tLUjh1d2FsTENJcndua3pFSmhxVWh5TVhra3pEMXZ1V2FIdVdFWkdlUVphU1lvcUFTckZkQnN6UGRiMDZiCnhvQlFWTWlxNEJDQjdoMXlNbWtMeUJJM0Q0aG9GcDE0M2VlaGNsdXpiUDJ5M3ZEZ2UxdTZoYkV2L2hYQjdWTTgKMlRlUkFHK3dzV2tPcW56YUdVd2JWVm9IaFN5SFpLdDNFcW1OMG5HTVQvL25US0cxbnYwRUFPMjVoaXA0SmUxego1SmQyL21wcUFZdzZrL2dMbDh2Yk04UjZqQ3N5dndzaXZFN0NjMi9oaDQ1ZXVDaHI4YlFlOXZ4UkhPMFlHNVB4Cmc3UkxPeXhPMzFyRDBpT0NrUUhMOWRlMzcrRXBheGZOcFl3c3lkRS9lb05QK3dJREFRQUJveU13SVRBT0JnTlYKSFE4QkFmOEVCQU1DQWdRd0R3WURWUjBUQVFIL0JBVXdBd0VCL3pBTkJna3Foa2lHOXcwQkFRc0ZBQU9DQVFFQQplMWN0dDZMSDU3d05mMUVuekp3SHJDemVLTHlOOGFrZ0x5ZDgzODE3UXJOVnphY0VhOGVIeVFXWkl0RXNRdWpVCnR6UEtGd3lUQXExWFVzRk1tSUZicHNoaGxrTU5wQVBLOStJZUxTb1FrcnRJZnpsREtZMmVKVCt0dlNuSzdnOU4KZU1Yc3c1N3NEbWNZcFZQOGVXS2xhWnE3M0RlRHhIdmdjcGc0eWJJNzkrQXMxMzYrRUlyNFJ0ZEhjcG55U1MwSAo1UnVPcW5aTlVrb1Jtd2dwU1E1MFV1TVlDU1V1WS9nTC9Rb0w0c1Z4ekM2eUNuNXdKcnA0TXZVdUxINWJNYlRiCk5aa2VzY2R0WEtISWZqU1FLMmNBM3lFOU92NnlveWYrY3grZm0xZGtRbnQ2bnFZVk5ieVB0UVFZMnRlZk4wZjgKVDlXaXNUN3VHaDdiYjluUzA1WFc5QT09Ci0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0K',
                    serverUrl: 'https://35.198.139.89',
                    contextName: 'gke_novatec-zweibruecken_europe-west3_nt-asca',
                    clusterName: 'gke_novatec-zweibruecken_europe-west3_nt-asca'
				 ]){
	  
	  	   sh("kubectl set image deployment/todobackend todobackend=${imageTag}")
	           sh("kubectl create configmap postgres-config --from-literal=postgres.db.name=mydb")
	   	   sh("kubectl create secret generic db-security --from-literal=db.user.name=matthias --from-literal=db.user.password=password")
                   sh("kubectl apply -f postgres.yaml")
                   sh("kubectl apply -f todobackend.yaml")
		  }
                   
	  }
  }

  
	
	
 
 
  
 
}

