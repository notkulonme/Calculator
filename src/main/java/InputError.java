import java.util.List;

public class InputError extends RuntimeException{
    public InputError(List<String> wrongNumbers){
        super("Wrongly formated number(s): " + String.join("; ", wrongNumbers));
    }
}
