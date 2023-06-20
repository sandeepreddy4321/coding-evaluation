package com.aa.act.interview.org;

import java.util.Optional;

public abstract class Organization {

    private Position root;
    // adding a parameter to track number of changes
    private int employeeId = 1;

    public Organization() {
        root = createOrganization();
    }

    protected abstract Position createOrganization();

    /** Dear Reader,
     * The public hire method is the entry point for hiring a person.
     * It first checks if the given title matches the title of the root position (the CEO position).
     * If it does, it proceeds to check if the root position is already filled.
     * If not, it assigns the person to the position, increments the employeeId, and returns an Optional containing the root position.
     * If the position is already filled, it throws an exception.
     */

    /**
     * hire the given person as an employee in the position that has that title
     *
     * @param person
     * @param title
     * @return the newly filled position or empty if no position has that title
     */
    public Optional<Position> hire(Name person, String title) {
        //if title is CEO it assigns to the given title else it calls the hire method to visit direct reports
        if (root.getTitle().equals(title)){
//            checks if the root is filled and assigns the name to the title else throws an error
            if (!root.isFilled()) {
                root.setEmployee(Optional.of(new Employee(employeeId, person)));
                employeeId += 1;
                return Optional.of(root);
            } else {
                throw new IllegalArgumentException("Position is already filled.");
            }
        }
        else {
            return hire(root, person, title);
        }
    }

   /**
    * The hire method is responsible for hiring a person for a specific title (job position) in the organization.
    * It takes a person's name and a title as input and returns an Optional<Position> representing the position where the person was hired.
    * If the position is already filled or cannot be found, it throws an exception or returns an empty Optional.
    */
    private Optional<Position> hire(Position currentPosition, Name person, String title) {
        // Visiting all the direct reports
        for (Position pos : currentPosition.getDirectReports()) {
            if (pos.getTitle().equals(title)) {
                // Assign employee if title matches else throws an error
                if (!pos.isFilled()) {
                    pos.setEmployee(Optional.of(new Employee(employeeId, person)));
                    employeeId += 1;
                    return Optional.of(pos);
                } else {
                    throw new IllegalArgumentException("Position is filled already!");
                }
            } else if (!pos.getDirectReports().isEmpty()) {
                // To Recursively visit the direct reports
                Optional<Position> newPos = hire(pos, person, title);
                if (newPos.isPresent()) {
                    return newPos;
                }
            }
        }
        return Optional.empty();
    }


    @Override
    public String toString() {
        return printOrganization(root, "");
    }

    private String printOrganization(Position pos, String prefix) {
        StringBuffer sb = new StringBuffer(prefix + "+-" + pos.toString() + "\n");
        for(Position p : pos.getDirectReports()) {
            sb.append(printOrganization(p, prefix + "  "));
        }
        return sb.toString();
    }
}
