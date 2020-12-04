package domain;

/**
 * @author Melton Smith
 * @since 03.12.2020
 */
public enum EppState {
    Forming, //формируется
    Acceptable, //на согласовании
    Accepted, //согласовано
    Declined, //отклонено
    Archived; //Архив

    public boolean isForming(){
        return this.equals(Forming);
    }

    public boolean isAcceptable(){
        return this.equals(Acceptable);
    }

    public boolean isDeclined(){
        return this.equals(Declined);
    }

    public boolean isAccepted(){
        return this.equals(Accepted);
    }

    public boolean isArchived(){
        return this.equals(Archived);
    }
}
