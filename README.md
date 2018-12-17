# OCS Optaplanner workshop

This document contains instructions for the OCS Optaplanner workshop. Most exercises come
from [Red Hat Optaplanner instructions bundle](http://www.optaplanner.org/learn/training.html).

## Lab 101: No more than 4 processes per computer

The customer’s use case closely resembles the OptaPlanner example CloudBalancing. But there’s 1 big difference:
* Don’t assign too many processes to the same computer

### Preparation

1. Create a new run configuration:
    1. Main class: org.optaplanner.examples.cloudbalancing.app.CloudBalancingApp
    2. VM parameters (optional): -Xmx512M -server

2. Run the run configuration.
    1. Quick open 4computer-12processes.xml, solve it and terminate solving early after about 20 seconds.
    2. Notice how the solution assigns 6 processes to computer 0.
    3. Leave this window open to compare it later with your modified version.

### Assignment
Add a new hard constraint: no computer should have more than 4 processes assigned to it.

### Success criteria
1. Open and solve 4computer-12processes.xml. It should no longer have a computer with more than 4 processes.
2. Open and solve 100computer-300processes.xml, in both the old and the new window. Compare the results.
    1. Both solutions should become feasible (0hard).
    2. The old window’s soft score is clearly better because it has more flexibility.

### Tips
* Add a new score rule in the file cloudBalancingScoreRules.drl. No other files require changes.
* Read up about [score traps](http://docs.optaplanner.org/latest/optaplanner-docs/html_single/index.html#scoreTrap), especially if 100computer-300processes.xml does not become feasible.

## Lab 102: No process should hog half the CPU power
The customer’s use case closely resembles the OptaPlanner example CloudBalancing. But there’s 1 big difference:
* Don’t let a process hog half the CPU power of a computer (unless the computer doesn’t have much CPU).
  
### Preparation
  
1. Create a new run configuration:
    1. Main class: org.optaplanner.examples.cloudbalancing.app.CloudBalancingApp
    2. VM parameters (optional): -Xmx512M -server    
2. Run the run configuration.
    1. Quick open 4computer-12processes.xml, solve it and terminate solving early after about 20 seconds.
    2. Click on the button Details of computer 3 to find out which process id’s are assigned to it.
    3. Notice how process 9 hogs 87.5% of the CPU of computer 3.
    4. Leave this window open to compare it later with your modified version.

### Assignment
1. A process is hogging the CPU of a computer if it requires more than half of the computer’s CPU power.
    1. Exception: A computer with 6 CPU or less is never considered hogged.
2. Add a new hard constraint: no process should hog the CPU of a computer.
3. The customer has decided that they want the hogging implementation formula in Java, not in DRL.
    1.But the constraint itself should be in DRL.

### Success criteria
1. Open and solve 4computer-12processes.xml. It should no longer assign process 9 to computer 3
    1. It must be feasible (0hard).
    2. Process 5 is (probably) still assigned to computer 1 and 2, because those computers have 6 CPU.
2. Open and solve 100computer-300processes.xml, in both the old and the new window. Compare the results.
    1. Both solutions should become feasible (0hard).
    2. The old window’s soft score is probably better because it has more flexibility.

### Tips
* Add a new Java method that checks if a process is hogging its computer.
* Add a new score rule in the file cloudBalancingScoreRules.drl and reuse that method.

## Lab 103: Distribute network bandwidth fairly
The customer’s use case closely resembles the OptaPlanner example CloudBalancing. But there are differences:
* The computers are already bought, so we might as well use them all
* All computers have the same network bandwidth.
* The network bandwidth should be load balanced (= distributed fairly) across all computers.

### Preparation
1. Create a new run configuration:
    1. Main class: org.optaplanner.examples.cloudbalancing.app.CloudBalancingApp
    2. VM parameters (optional): -Xmx512M -server
    
2. Run the run configuration.
    1. Quick open 100computer-300processes.xml, solve it and terminate solving early after about 20 seconds.
    2. Notice how the used network bandwidth heavily differs per computer (and that capacity clearly limits it).
    3. Leave this window open to compare it later with your modified version.

### Assignment
* Remove the hard constraint requiredNetworkBandwidthTotal
* Remove the soft constraint computerCost
* Add a new soft constraint: The network bandwidth used per computer should be as equal as possible (evenly distributed).

_Note_: The GUI will still show the network bandwidth capacity per computer and color it red. Just ignore that.

### Success criteria
1. Open and solve 100computer-300processes.xml.
    1. It must be feasible (0hard).
    2. All computers must be used.
    3. The total network bandwidth used per computer must be close to each other, except if the computer has a single process which uses far more network bandwidth.
2. Compare the results with the old window.

### Tips
*Remove 2 score rules and add a new score rule in the file cloudBalancingScoreRules.drl. No other files require changes.
*Read up about [fairness score constraints](http://docs.optaplanner.org/latest/optaplanner-docs/html_single/index.html#fairnessScoreConstraints).
*There is no law which says that a score (hard or soft) should converge to 0.

## Lab 901: Game the US elections
Decide in which US states half of the population plus one should vote for the gamer candidate, for that candidate to win with as little votes as possible.

### Constraints
Presume everyone US citizen votes and there are only 2 candidates.

Hard constraints:
* The gamer candidate must collect at least 270 EC votes.
Soft constraints:
* Minimize the number of votes for the gamer candidate.

### Tips
* Try to speedup the process by setting up [Difficulty Comparator](https://docs.optaplanner.org/7.14.0.Final/optaplanner-docs/html_single/index.html#planningEntity) and a [Strength Comparator](https://docs.optaplanner.org/7.14.0.Final/optaplanner-docs/html_single/index.html#planningValueAndPlanningValueRange).

## Lab 905: Vehicle routing problem - Arrival time
Customers can order deliveries to be made before a specific time. The set of rules contains
a rule "arrivalAfterDueTime" that enforces a delivery to be made before its due time. The
entity TimeWindowedCustomer contains a variable "arrivalTime". The arrivalTime at a Customer
depends on previous visits. Because of this the variable needs to be updated after the solver
makes a change. As the ShadowVariable annotations says there is a listener class that listens
to changes made to the previousStandstill (the trigger to update this variable).

### Preparation
1. Create a new run configuration:
    1. Main class: org.optaplanner.examples.vehiclerouting.app.MixedVehicleRoutingApp
    2. VM parameters (optional): -Xmx512M -server
    
With the code in the current state the solver cannot compute a solution yet.

### Assignment
Fill in the holes in the org.optaplanner.examples.vehiclerouting.domain.timewindowed.solver.ArrivalTimeUpdatingVariableListener
class.

### Success criteria:
* All trips are delivered before their due time.
* Use the existing logic for computing the arrivalTime
* Enable full assert to verify the working the VariableListener

Use one of the cvrptw input files. 

### Tips:
[Documentation on shadow variables](https://docs.optaplanner.org/7.15.0.Final/optaplanner-docs/html_single/index.html#shadowVariable)
Hint before setting a variable relevant to the planning engine (PlanningVariables and ShadowVariables)
notify the solver about this using scoreDirector.beforeVariableChanged method.

Constructions very similar are useful when making changes to the plan while solving.

[Documentation on environment modes](https://docs.optaplanner.org/7.15.0.Final/optaplanner-docs/html_single/index.html#environmentMode)

## Lab 906: Mixed Vehicle Routing Problem - Pickup before delivery
This lab uses the code written by Geoffrey de Smet for the mixed vehicle routing problem.

For a package delivery service that picks up and delivers packages it is important that
for a 'Ride' the contained 'Visit's are made by a single vehicle. And the pickup visit
is performed before the delivery Visit.

### Preparation
1. Create a new run configuration:
    1. Main class: org.optaplanner.examples.vehiclerouting.app.MixedVehicleRoutingApp
    2. VM parameters (optional): -Xmx512M -server
    
2. Run the run configuration.
    1. Quick open cvrp-32customers, solve it and terminate solving early after about 20 seconds.
    2. Notice how not all pickup and deliveries that were initially connected are not serviced by
    a single vehicle.
    3. Leave this window open to compare it later with your modified version.
    
### Assignment
Define hard constraints that:
* Implement the VisitIndexVariableListener
* Enforces a pickup and delivery visit of a ride are performed by a single truck
* Pickup visit is made before a delivery visit using the 'visitIndex' variable

### Success criteria:
* All pickups and deliveries of a single ride are made by a single vehicle
* The pickup visit of a ride is always scheduled before the delivery visit
* Enable full assert to verify the working the VariableListener
