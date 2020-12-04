package services;

import java.util.HashSet;

/**
 * @author Melton Smith
 * @since 01.12.2020
 */
public class IdGenerator {

    public static final IdGenerator INSTANCE = new IdGenerator();
    private static final HashSet<Long> EXISTING_ID = new HashSet<Long>();

    private IdGenerator(){ }

    public long generateId(){
        long idCandidate =  (long) (Math.random() * Long.MAX_VALUE);
        boolean unique = EXISTING_ID.add(idCandidate);

        if (unique)
            return idCandidate;

        return generateId();
    }
}
