//Name: Elisei Iovuta
//Date: 09.02.2022
//For: IBM CIC

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("---------------DC TOWER ELEVATOR SIMULATION----------------");
		
		//Set up Tower/ gives the option of deciding the number of floors
		
		DCTower tower = new DCTower(55);
		
		/*Adds elevators to Tower/ calls the addListener() 
		that makes the elevators react to the interface IRequest*/
		
		for (int i = 0; i < 7; i++) {
			tower.addListener(new Elevator(i+1,tower));
		}
		
		//--------------------TESTING------------------------
		
		//Extremities
		tower.addRequest(0, 55, Direction.up);
		tower.addRequest(55, 0, Direction.down);
		
		//Multiple Valid Requests: upwards
		tower.addRequest(0, 1, Direction.up);
		tower.addRequest(0, 11, Direction.up);
		tower.addRequest(0, 18, Direction.up);
		tower.addRequest(0, 26, Direction.up);
		tower.addRequest(0, 46, Direction.up);
		
		//Multiple Valid Requests: downwards
		tower.addRequest(41, 0, Direction.down);
		tower.addRequest(10, 0, Direction.down);
		tower.addRequest(2, 0, Direction.down);
		tower.addRequest(18, 0, Direction.down);
		tower.addRequest(7, 0, Direction.down);
		
		//Multiple Invalid Requests: Direction not matching
		tower.addRequest(55, 0, Direction.up);
		tower.addRequest(10, 0, Direction.up);
		tower.addRequest(0, 22, Direction.down);
		tower.addRequest(0, 11, Direction.down);
		
		/*Multiple Invalid Requests: Denied Access to travel 
		 * between other floors other than the ground 
		 * floor and the accessed floor*/
		tower.addRequest(12, 14, Direction.up);
		tower.addRequest(10, 40, Direction.up);
		tower.addRequest(44, 22, Direction.down);
		tower.addRequest(55, 11, Direction.down);
		
		//Multiple Invalid Requests: Position or Destination not existent
		tower.addRequest(72, 0, Direction.down);
		tower.addRequest(120, -16, Direction.down);
		tower.addRequest(-100, 222, Direction.up);
		tower.addRequest(0, -11, Direction.down);
		
		
		
	}

}
