package connections.tcp.instructions.distribution;

public class InstructionUtils {
    public static long parseInstructionId(String instruction) {
        return Long.parseLong(getStringAtPosition(instruction, 0));
    }

    public static String parseInstructionType(String instruction) {
        return getStringAtPosition(instruction, 1);
    }

    public static String parseNameWithSpaces(String instruction, int startingSlot) {
        String[] splitInstruction = instruction.split(" ");
        if (splitInstruction.length <= startingSlot) {
            throw new RuntimeException("Starting slot cannot be past end of string.");
        }

        StringBuilder result = new StringBuilder();
        for (int i = startingSlot; i < splitInstruction.length; i++) {
            if (i > 0) {
                result.append(" ");
            }
            result.append(splitInstruction[i]);
        }

        return result.toString();
    }

    private static String getStringAtPosition(String instruction, int position) {
        String[] splitInstruction = instruction.split(" ");
        if (splitInstruction.length <= position) {
            throw new RuntimeException("No spaces found in split instruction string of length "
                    + splitInstruction.length + " to accommodate position " + position + ". " +
                    "Instruction string: " + instruction);
        }

        return splitInstruction[position];
    }
}
