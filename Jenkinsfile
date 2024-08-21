pipeline {
    agent any

    tools {
        jdk 'JDK21'
    }

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials-id')
        DOCKER_IMAGE = credentials('docker-image')
        K8S_URL = credentials('k8s-url')
        K8S_NAMESPACE = credentials('k8s-namespace')
        K8S_DEPLOY_NAME = credentials('k8s-deploy-name')
        JAVA_HOME = "${tool 'JDK21'}"
        PATH = "${env.JAVA_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                script {
                    sh './gradlew openapi3'
                    sh './gradlew clean build'
                }
            }
            post {
                failure {
                    error 'Deployment failed!'
                }
            }
        }

        stage('Login'){
            steps{
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin' // docker hub 로그인
            }
        }

        stage('Build & Push Docker Image') {
            steps {
                script {
                    sh 'docker buildx build --push --platform linux/amd64 --build-arg PROFILE=prod -t $DOCKER_IMAGE .'
                }
            }
        }

        stage('ssh-test') {
            steps{
                script{
                    sshagent (credentials: ['ncp-key']) {
                        sh  """
                        ssh -o StrictHostKeyChecking=no ${K8S_URL} << EOF
                        microk8s kubectl rollout restart deploy ${K8S_DEPLOY_NAME} -n=${K8S_NAMESPACE}
                        """
                    }
                }
            }
        }

    }

    post {
        success {
            echo 'Deployment was successful!'
        }
        failure {
            error 'Deployment failed!'
        }
    }
}
