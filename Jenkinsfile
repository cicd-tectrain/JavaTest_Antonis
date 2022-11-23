//Basis Pipeline Schema anlegen

pipeline {
  agent any

    //create environment
    environment {
        INTEGRATION_BRANCH = 'intergration'
        PRODUCTION_BRANCH = 'master'
    }

  stages{
        stage('Building Feature') {
          when {
                  branch 'feature/*'
                  beforeAgent true
                }

          //Agent overwrite and run in a docker container
          agent {
            docker {
                image 'gradle:7.5.1-jdk17-focal'
            }
          }


          steps {
            echo 'Building feature'
            sh 'ls -la'
            sh 'gradle clean build -x test'
          }
        }

        stage('Testing Feature') {
          when {
                  branch 'feature/*'
                  beforeAgent true
                }

        //Agent overwrite and run in a docker container
          agent {
            docker {
                image 'gradle:7.5.1-jdk17-focal'
            }
          }


          steps {
            echo 'Testing feature'
            sh 'gradle test'
          }

          //post-build
          post {
            always {
                //collect JUnit test  results
                junit 'build/test-results/**/*.xml'
            }
            success {
                        publishHTML([allowMissing: true, alwaysLinkToLastBuild: false, keepAll: true, reportDir: 'build/reports/tests/test/', reportFiles: 'index.html', reportName: 'HTML Report', reportTitles: ''])
            }
          }

        }

        stage('Integrating Feature') {
          when {
            branch 'feature/*'
            beforeAgent true
          }

          steps {
                  echo 'Integrating feature'
                  sh 'ls -la'
                  sh 'git branch -a'
                  sh 'git checkout ${BRANCH_NAME}'
                  sh 'git pull'
                  sh 'git checkout ${INTEGRATION_BRANCH}'
                  sh 'git merge ${BRANCH_NAME}'
                  withCredentials ([
                        gitUsernamePassword(credentialsId: 'github_cicd_pat', gitToolName: 'Default')
                  ]) {
                        sh 'git push origin ${INTEGRATION_BRANCH}'
                  }
          }
        }

        // ====== Integration Stages ======
        stage('Build integration branch') {
            when {
                branch 'intergration'
                beforeAgent true
            }

            //Agent overwrite and run in a docker container
            agent {
                docker {
                    image 'gradle:7.5.1-jdk17-focal'
                }
            }

            steps {
                echo 'Building integration'
                sh 'gradle clean build -x test'
            }

            post {
                success {
                    //stash build directory for later use
                    stash includes: 'build/', name: 'build'
                }
            }
        }

        stage('Test intergration branch') {
            when {
                branch 'intergration'
                beforeAgent true
            }

            //Agent overwrite and run in a docker container
            agent {
                docker {
                    image 'gradle:7.5.1-jdk17-focal'
                }
            }

            steps {
                echo 'Testing integration'
                sh 'gradle test'
            }
        }


        stage ('Publish artifacts') {

            when {
                branch 'intergration'
                beforeAgent true
            }

            agent {
                docker {
                    image 'gradle:7.5.1-jdk17-focal'
                }
            }

            steps {
                echo 'Publishing artifacts'
                sh 'ls -la'

                nexusArtifactUploader artifacts: [[
                    artifactId: 'at.tectrain.cicd',
                    classifier: '',
                    file: 'build/libs/demo-0.0.1-SNAPSHOT.jar',
                    type: 'jar'
                ]],
                credentialsId: 'nexus_credentials',
                groupId: '',
                nexusUrl: 'nexus:8081/repository/maven-snapshots',
                nexusVersion: 'nexus3',
                protocol: 'http',
                repository: '',
                version: '0.0.1-SNAPSHOT'
            }
        }


        stage('Deploy intergration branch') {
            when {
                branch 'intergration'
                beforeAgent true
            }

            environment {
                NEXUS = credentials('nexus_credentials')
            }

            steps {
                echo 'Deploying integration'

                //unstash build directory
                unstash 'build'
                sh 'ls -la build'


                //display info about docker
                sh 'docker info'
                sh 'docker compose version'
                sh 'docker compose config'


                //build testing image using docker compose
                sh 'docker compose build testing'

                //login at nexus docker registry
                sh 'echo $NEXUS_PSW > pw.txt'
                sh 'cat pw.txt'
                sh 'echo $NEXUS_PSW | docker login --username $NEXUS_USR --password-stdin nexus:5000'

                //push image to registry
                sh 'docker compose push testing'

                //redeploy testing container
                sh 'docker compose up -d --force-recreate testing'
            }

            //post: logout docker
            post {
                always {
                    sh 'docker logout nexus:5000'
                    //delete /var/jenkins_home/.docker/config.json
                }
            }

        }

        stage ('Merge integration into master')
        {
            when {
                branch 'intergration'
                beforeAgent true
            }


            steps {
                echo 'Merge into master'
                sh 'ls -la'
                sh 'git branch -a'
                sh 'git checkout ${BRANCH_NAME}'
                sh 'git pull'
                sh 'git checkout ${PRODUCTION_BRANCH}'
                sh 'git merge ${BRANCH_NAME}'
                withCredentials ([
                    gitUsernamePassword(credentialsId: 'github_cicd_pat', gitToolName: 'Default')
                ]) {
                sh 'git push origin ${PRODUCTION_BRANCH}'
                    }
            }
        }

        // ====== Master Stages ======
        stage('Build master branch') {
            when {
                branch 'master'
                beforeAgent true
            }

            //Agent overwrite and run in a docker container
            agent {
                docker {
                    image 'gradle:7.5.1-jdk17-focal'
                }
            }

            steps {
                echo 'Building master'
                sh 'gradle clean build -x test'
            }

            post {
                success {
                    //stash build directory for later use
                    stash includes: 'build/', name: 'build'
                }
            }
        }

        stage('Test master branch') {
            when {
                branch 'master'
                beforeAgent true
            }

            //Agent overwrite and run in a docker container
            agent {
                docker {
                    image 'gradle:7.5.1-jdk17-focal'
                }
            }

            steps {
                echo 'Testing master'
                sh 'gradle test'
            }
        }


        stage ('Publish production artifacts') {

            when {
                branch 'master'
                beforeAgent true
            }

            agent {
                docker {
                    image 'gradle:7.5.1-jdk17-focal'
                }
            }

            steps {
                echo 'Publishing artifacts'
                sh 'ls -la'

                nexusArtifactUploader artifacts: [[
                    artifactId: 'at.tectrain.cicd',
                    classifier: '',
                    file: 'build/libs/demo-0.0.1-SNAPSHOT.jar',
                    type: 'jar'
                ]],
                credentialsId: 'nexus_credentials',
                groupId: '',
                nexusUrl: 'nexus:8081/repository/maven-releases',
                nexusVersion: 'nexus3',
                protocol: 'http',
                repository: '',
                version: '0.0.1-SNAPSHOT'
            }
        }

        stage('Deploy master branch') {
            when {
                branch 'master'
                beforeAgent true
            }

            environment {
                NEXUS = credentials('nexus_credentials')
            }

            steps {
                echo 'Deploying master'

                //unstash build directory
                unstash 'build'
                sh 'ls -la build'


                //display info about docker
                sh 'docker info'
                sh 'docker compose version'
                sh 'docker compose config'


                //build testing image using docker compose
                sh 'docker compose build production'

                //login at nexus docker registry
                sh 'echo $NEXUS_PSW > pw.txt'
                sh 'cat pw.txt'
                sh 'echo $NEXUS_PSW | docker login --username $NEXUS_USR --password-stdin nexus:5000'

                //push image to registry
                sh 'docker compose push production'

                //redeploy testing container
                sh 'docker compose up -d --force-recreate production'
            }

            //post: logout docker
            post {
                always {
                    sh 'docker logout nexus:5000'
                    //delete /var/jenkins_home/.docker/config.json
                }
            }

        }

  }
}