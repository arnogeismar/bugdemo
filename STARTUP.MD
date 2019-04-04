1) in this folder do docker-compose up -d
2) goto localhost:15672 and login with guest, guest
3) create a queue named test-queue and bind it to the amq.direct exchange
4) create a new user with password admin and username admin
5) give the user rights on all queues
6) start the application and furiously click the button 
7) you will see that the output is random and sometimes is the child output and sometimes the parent output