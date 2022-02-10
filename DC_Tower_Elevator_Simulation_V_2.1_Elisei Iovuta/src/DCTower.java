import java.util.ArrayList;

//Simulation Class
public class DCTower implements IResponse{

	//Tower maximal number of floors
	private int towerFloors = 0;
	
	//Array List of all the Elevators acts as Listener list for the IRequest events
	private ArrayList<IRequest> elevators = new ArrayList<IRequest>();
	
	//DC Tower/ Simulation Class Constructor
	public DCTower(int towerfloors) {
		this.towerFloors = towerfloors;
		//Generates the elevators and starts the threads
		
	}
	
	//Request method of the tower
	public void addRequest(int callerPosition, int callerDestination, Direction callDirection) {
		
		//calls the requestError method, gives out the request data and breaks the request
		if (requestIfError(callerPosition, callerDestination, callDirection)) {
			System.out.println("ERROR AT POSTION: "+callerPosition+" DESTINATION: "+callerDestination+" DIRECTION "+callDirection);
			return;
		}
		
		//variable to store weather all the elevators are unavailable
		boolean allElevatorsUnavailable = true;
		
		/*Checks elevators for availability, if one is found, 
		 * the "for each" loop is broken and a request is made*/
		for (int elevator = 0; elevator < elevators.size(); elevator++) {
			if (elevators.get(elevator).isAvailable() == true) {
				allElevatorsUnavailable = false;
				elevators.get(elevator).requestElevator(callerPosition, callerDestination, callDirection);
				break;
			}
			
		}
		
		/*Variables to store data of the next elevator 
		 * that is fit to run the request*/
		int fewestFloorsToGo = towerFloors;
		IRequest nextElevatorRequest = null;
		
		/*If all Elevators are unavailable the floorsToGo event requests from all 
		 * elevators how many floors there are to go in order 
		 * to reach the call position and destination*/
		if (allElevatorsUnavailable) {
			for (int elevator = 0; elevator < elevators.size()-1; elevator++) {
				int floorsToGo = elevators.get(elevator).floorsToGo(callerPosition);
				if (fewestFloorsToGo >= floorsToGo) {
					nextElevatorRequest = elevators.get(elevator);
					fewestFloorsToGo = floorsToGo;
				}
			}
			
			//the elevator with the fewest floors to go is getting the request 
			nextElevatorRequest.requestElevator(callerPosition, callerDestination, callDirection);
		}
	}

	//Add Listener Method that fills the elevators list
	public void addListener(IRequest elevator) {
		//Adds to list
		elevators.add(elevator);
	}
	
	//Checks for invalid requests
	private boolean requestIfError(int callerPosition, int callerDestination, Direction callDirection) {
		
		//Checks if a Request was made to an nonexistent Position/Destination
		if (callerPosition > towerFloors || callerPosition < 0 
				|| callerDestination < 0 || callerDestination > towerFloors) {
			
			System.out.println("--------------------------------------------------------------");
			System.out.println("ERROR: Request was made to an nonexistent Position/Destination");
			System.out.println("--------------------------------------------------------------");
			return true;
		}
		
		//Checks if a Request with a direction non-corresponding to the position and destination was made
		if (callerPosition<callerDestination && callDirection == Direction.down 
				|| callerPosition > callerDestination && callDirection == Direction.up) {
			
			System.out.println("---------------------------------------------------------------------------------");
			System.out.println("ERROR: Request with a direction non-corresponding to the position and destination");
			System.out.println("---------------------------------------------------------------------------------");
			return true;
		}
		
		//Checks if a Request was made with an inactive direction
		if (callDirection == Direction.inactive) {
			System.out.println("--------------------------------------------------");
			System.out.println("ERROR: Request was made with an inactive direction");
			System.out.println("--------------------------------------------------");
			return true;
		}
		
		//Checks if a Request was made from one floor to another (not from or to the ground floor)
		if (callerDestination != 0 && callerPosition != 0) {
			System.out.println("------------------------------------------------------------------------------------");
			System.out.println("ERROR: ACCESS DENIED for inter-floor travel, you can only return to the ground floor");
			System.out.println("------------------------------------------------------------------------------------");
			return true;
		}
		/*If none of the criteria is met, the request lacks
		 * errors and can proceed to be executed, thus returns false*/
		return false;
	}
	
	//Synchronized event method that write on the console the results of the Elevators when a pick up has been made
	@Override
	public synchronized void pickedUp(int elevatorId, int floor) {
		System.out.println("Elevator "+elevatorId+" picks up from floor "+floor+"!");
		
	}
	
	//Synchronized event method that write on the console the results of the Elevators when a drop off has been made
	@Override
	public synchronized void dropedOff(int elevatorId, int floor) {
		System.out.println("Elevator "+elevatorId+" drops off at floor "+floor+"!");
		
	}

}
