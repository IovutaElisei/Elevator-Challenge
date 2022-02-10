import java.util.LinkedList;
import java.util.Queue;

public class Elevator implements Runnable, IRequest{

	//Elevator ID and Tower Reference
	private int elevatorID;
	private IResponse tower;
	
	//Elevator Thread
	private Thread thread = new Thread(this);
	
	//Elevator Position, Direction and Destination Variables
	private int position;
	private int destination;
	private Direction direction;
	
	//Position, Direction and Destination Queues storing the next requests for an elevator
	
	/*This part can be improved by making a class Request() 
	 * that contains the three data pieces and a queue 
	 * can then be made as Queue<Request> nextCalls*/
	
	private Queue<Integer> nextCallPosition = new LinkedList<Integer>();
	private Queue<Integer> nextCallDestination = new LinkedList<Integer>();
	private Queue<Direction> nextCallDirection = new LinkedList<Direction>();
	
	//Integer variables that store the requested pick up and drop off positions
	private int pickUp = -1;
	private int dropOff = -1;
	
	//Boolean variable that stores weather a direction switch has to take place or not
	private boolean switchDirection = false;
	
	/*Elevator Constructor: sets all new elevators inactive at 
	 * floor 0 with a destination at floor 0 with an unique id 
	 * number and a reference to the Tower*/
	public Elevator(int id, IResponse tower) {
		this.elevatorID = id;
		this.tower = tower;
		thread.start();
		
		this.position = 0;
		this.destination = 0;
		this.direction = Direction.inactive;
	}
	
	//Thread run method
	public void run() {
		
		//endless loop 
		/*(Can be improved upon by adding a logical criteria 
		 * and putting the thread to sleep if criteria isn't met)*/
		while(true) {
			
			//repeats as long as the direction is upwards and the destination hasn't been reached
			while(direction==Direction.up && position < destination) {
				
				/*if the pick up position has been reached, the IResponse 
				 * interface is set off sending the elevator id and its current position */
				if (position==pickUp) {
					tower.pickedUp(elevatorID, position);
				}
				
				//Elevator moves upwards incrementally by one floor for every run inside the loop
				position++;
				
				//TEST Console Text
				//System.out.println("Test elevator moves by one floor "+position);
				 
				/*Is the destination reached with no direction switch, the dropedOff 
				 * event is called informing the Tower of the successful drop*/
				if (position == destination && switchDirection == false) {
					tower.dropedOff(elevatorID, position);
					this.direction = Direction.inactive;
					
					//If there's a queued up Request, that request is called now and is removed from the queue
					if (nextCallDestination.isEmpty() != true) {
						this.requestElevator(nextCallPosition.poll(), nextCallDestination.poll(), nextCallDirection.poll());
					}
				}
				
				/*If the destination is reached but a switch has to take place, 
				 * the drop off destination is set, 
				 * the switch is removed and the direction set downwards*/
				else if(position == destination && switchDirection == true) {
					//System.out.println("Switch");
					this.destination = dropOff;
					switchDirection=false;
					direction= Direction.down;
				}
			}
			
			//repeats as long as the direction is downwards and the destination hasn't been reached
			while(direction == Direction.down && position > destination) {
				/*if the pick up position has been reached, the IResponse 
				 * interface is set off sending the elevator id and its current position */
				if (position==pickUp) {
					tower.pickedUp(elevatorID, position);
				}
				
				//Elevator moves downwards incrementally by one floor for every run inside the loop
				position--;
				 
				
				/*Is the destination reached with no direction switch, the dropedOff 
				 * event is called informing the Tower of the successful drop*/
				if (position == destination && switchDirection == false) {
					tower.dropedOff(elevatorID, position);
					this.direction = Direction.inactive;
					
					//If there's a queued up Request, that request is called now and is removed from the queue
					if (nextCallDestination.isEmpty() != true) {
						this.requestElevator(nextCallPosition.poll(), nextCallDestination.poll(), nextCallDirection.poll());
					}
				}
				
				/*If the destination is reached but a switch has to take place, 
				* the drop off destination is set, 
				* the switch is removed and the direction set downwards*/
				else if(position == destination && switchDirection == true) {	
					this.destination = dropOff;
					switchDirection = false;
					direction = Direction.up;
				}
			}
		 }
	}

	/*event method with a true/false return variant, 
	 * gives out if an elevator is available for request*/
	@Override
	public boolean isAvailable() {
		if (this.direction == Direction.inactive) {
			return true;
		}
		return false;
	}

	
	/*Event method that requests an elevator to a certain position 
	 * going to a certain destination in one of two directions*/
	@Override
	public void requestElevator(int callerPosition, int callerDestination, Direction callDirection) {
		
		
		System.out.println("Elevator "+elevatorID+" REQUESTED AT FLOOR: "+callerPosition+" HEADING "+callDirection+" TOWARDS FLOOR: "+callerDestination);
		
		/*If an elevator is active and is still requested, 
		 * the Request is added to a queue to be executed 
		 * after the current request is finished*/
		
		if (this.direction != Direction.inactive) {
			System.out.println("ADDED TO QUEUE Elevator"+elevatorID+" REQUEST " );
			nextCallDestination.add(callerDestination);
			nextCallDirection.add(callDirection);
			nextCallPosition.add(callerPosition);
		}
		else {
		/*the pick up call location and the drop off call destination 
		 * are saved for later use of the IResponse*/
			
		this.pickUp = callerPosition;
		this.dropOff = callerDestination;
		
		/*is the called destination higher than the last saved destination 
		 * as well as the direction of the call, being upwards: 
		 * the destination is set as the call destination*/
		
		if(destination <= callerDestination && callDirection == Direction.up) {
			this.destination=callerDestination;
			this.direction = Direction.up;
		}
		
		/*is the called destination lower than the last saved destination 
		 * as well as the direction of the call, being downwards: 
		 * the destination is set as the call destination*/
		
		if(destination > callerDestination && callDirection == Direction.down) {
			this.destination=callerDestination;
			this.direction = Direction.down;
		}
		
		/*is the call direction downwards and the current elevator position 
		 * is below the pick up position, the direction of the call is 
		 * switched and the pick up position is set as a temporary destination*/
		
		if(callDirection == Direction.down && this.position < callerPosition) {
			this.direction = Direction.up;
			this.destination = pickUp;
			this.switchDirection = true;
		}
		
		/*is the call direction upwards and the current elevator position 
		 * is above the pick up position, the direction of the call is 
		 * switched and the pick up position is set as a temporary destination*/
				
		if(callDirection == Direction.up && this.position > callerPosition) {
			this.direction = Direction.down;
			this.destination = pickUp;
			this.switchDirection = true;
		}
		
		}
		
	}

	/*Method that returns how many floors an elevator has 
	 * to go through to get to the called position*/
	@Override
	public int floorsToGo(int callerPosition) {
		if (dropOff>callerPosition) {
			return dropOff-callerPosition;
		}
		else if (dropOff>callerPosition) {
			return callerPosition-dropOff;
		}
		return 0;
	}
}
