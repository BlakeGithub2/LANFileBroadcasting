package connections.tcp.instructions.distribution;

public record InstructionResponse(long instructionId, Object returnValue) {
    public InstructionResponse(long instructionId, Object returnValue) {
        this.instructionId = instructionId;
        this.returnValue = returnValue;
    }
}
