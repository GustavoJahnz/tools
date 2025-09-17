package challenge.tools.util.exception;

public record ErrorResponseItem(

        String item,

        String reason,

        Object rejectedValue
) {
}
