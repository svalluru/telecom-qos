Pre-reqs : 

- Start MySQL DB : docker run -p 3306:3306 --name=sri-mysql --env="MYSQL_ROOT_PASSWORD=mypassword" mysql
	Create below table : 
		CREATE TABLE account_closed (
	    phoneno varchar(255),
	    unlockphone boolean,
	    phoneunlocked boolean,
	    eDelivery boolean,
	    finalamount varchar(255),
	    finalamountpaid boolean,
	    accountclosed boolean,
	    finalbillnotificationsent boolean,
	    daterequested varchar(255),
	    comments varchar);
		

- Start JBoss with PAM
	CD /Users/svalluru/EAP-7.3.0/bin and Start Jboss EAP standalone.sh -c standalone-full.xml
	Go to PAM console : http://localhost:8080/business-central/kie-wb.jsp 
	
- Start Kafka and HTTP Bridge
	Go to /Users/svalluru/Downloads/kafka-bridge-0.19.0/bin and ./kafka_bridge_run.sh --config-file=../config/application.properties

- Clone https://github.com/svalluru/telecom-qos-ui and start using "ng serve" then go to http://localhost:4200/


Follow the below steps if you are not using UI to test it.

Happy Path : 

Put Below JSON message in close-account topic

{
	"phoneno" : "6508621000",
	"unlock" : "false",
	"eDelivery" : "true"
}

Update DB with following statement : 

update account_closed Set finalamountpaid=true where phoneno = '6508621000'


Unlock devide failed Path : 

Put Below JSON message in close-account topic

{
	"phoneno" : "6508621001",
	"unlock" : "true",
	"eDelivery" : "true"
}

Update DB with following statement : 

update account_closed Set finalamountpaid=true where phoneno = '6508621001'

Start PAM task and complete it and check if the DB process is done or not.







