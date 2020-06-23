***********
Introduction:
***********
vote-event-api is a stand alone SpringBoot Application that manages the VoteEvents.

************************************
This API exposes the below methods
************************************
1. Create a vote Event with Name , expiryDate and List of Options.
2. Update a Vote Event
3. Add a vote to the VoteEvent as long as the VoteEvent is active and accepting votes and the vote is not duplicate.
4. Get all VoteEvents
5. Get a Vote Event By Id.
6. Get Results of the vote after the vote is closed.
7. Get Results of the vote split by Age , Gender and Locality

******************************************************************************
Design of the DB Tables. This is mapped to Todo.java through ORM(Hibernate).
******************************************************************************
VoteEvent:

Column			Description
id				Id of the VoteEvent. Auto Generated Primary Key
name			Name of the VoteEvent
expiryDate		Date on which the VoteEvent will expire(Post which votes will not be accepted)
listOfOptions	List of Options on the Vote
listOfVotes  	List of votes Registered for the VoteEvent

Vote:
Column			Description
id				Id of the Vote. Auto Generated Primary Key
name			Name of the Voter
age				Age of the Voter
gender			Gender of the Voter
locality		Locality of the Voter
votingOption	Voting Option of the Voter

******************
List of end points:
******************

HTTP Method	URL													Description
GET			/voteEvents/										Retrieves the list of VoteEvents from the Database
GET			/voteEvents?showActive=true							Retrieves the list of Active VoteEvents from the Database
GET			/voteEvents/{voteEventId}  							Retrieves the specific VoteEvent with the id 
POST		/voteEvents/			 							Creates a VoteEvent and saves to the  Database
PUT			/voteEvents/{voteEventId}							Updates the VoteEvent with the id in the Database
POST		/voteEvents/{voteEventId}/vote						Adds a Vote to the VoteEvent(IF the Vote is expired or if the vote is duplicate(with  																same name/age/gender/locality combination), the vote is rejected with Forbidden 																Exception
GET			/voteEvents/{voteEventId}/voteResults				Retrieves the Vote Results. If the vote is ongoing, thows a Forbiden Exception
GET			/voteEvents/{voteEventId}/voteResults?splitBy=age	Splits the Reults by Age. If the vote is ongoing, thows a Forbiden Exception
GET			/voteEvents/{voteEventId}/voteResults?splitBy=gender Splits the Reults by Gender. If the vote is ongoing, thows a Forbiden Exception

******************
Package Structure:
******************
All the java classes are organized in com.anusha.projects.springboot.votemanagement for the sake of simplicity.

Name						Purpose
Vote.java					Model for Votes
VoteEvent.java				Model for VoteEvents
VoteResult.java				Model for VoteResults
VoteEventApplication.java	Main class which starts the Springboot application
VoteEventController.java	Controller which handles the requests from client and invokes service methods
VoteEventService.java		Service class that handles the actual business logic and has calls to DB
VoteRepository.java			DAO Class which uses Springboot's inbuilt Repository implementation for some of the common DB calls.
VoteEventRepository.java	DAO Class which uses Springboot's inbuilt Repository implementation for some of the common DB calls.

Exception classes are placed in com.anusha.projects.springboot.votemanagement.exception package.
ForbiddenException.java		Thrown when the operation is not permitted.
NotFoundException			Thrown when the requested item is not found

All test classes are under the src/test/java folder.

*************************
Development Environment:
*************************
Developed using Eclipse Photon on Ubuntu.
Built using Maven. 

******************************
Steps to Run the Application:
******************************
The JAR file can be generated using maven.

To run the application - Goto the terminal (command line)  and type the below command:
java -jar <the location of the JAR file>

This will start the Springboot application and you are ready to fire requests to the above mentioned URL s.
It works with in-memory Apache Derby database for all the data operations.

***********************
Sample JSON -VoteEvent POST
***********************

	{
        "name": "Does social media positively influence the younger Generation?",
        "expiryDate": "2018-06-30",
        "listOfOptions" : ["Yes", "No" ,"Maybe"]
     }
 
***********************
Sample JSON - Vote POST 
***********************

	{
        "name": "Elsa",
        "age": "19",
        "gender": "F",
        "locality": "Helsinki",
        "votingOption": "Yes"
    }
