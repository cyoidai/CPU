import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CacheTest {
    @Test
    public void sumTest() {
        String[] program = {
                "copy 10 r0",  // 0 let r0=10 array start pointer
                "copy 15 r1",  //   let r1=29 array end pointer
                "add 14 r1",   // 1
                "copy 1 r2",   //   let r2 value to enter
                "store r2 r0", // 2
                "add 1 r0",
                "add 1 r2",    // 3
                "compare r0 r1",
                "ble -2",      // 4
                "copy 10 r0",  //   reset array start pointer
                "copy 0 r2",   // 5 let r2 be the array sum
                "copy 0 r3",   //   let r3 be the current value from the array
                "load r0 r3",  // 6
                "add r3 r2",   //
                "add 1 r0",    // 7
                "compare r0 r1",
                "ble -2",      // 8
                "syscall 0"
        };
        Processor p = runProgram(program);
        assertEquals("r2:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,t,f,t,f,f,t,f,", p.output.get(2)); // =210
    }

    @Test
    public void sumBackwardsTest() {
        String[] program = {
                "copy 10 r0",  // 0 let r0=10 array start pointer
                "copy 15 r1",  //   let r1=29 array end pointer
                "add 14 r1",   // 1
                "copy 1 r2",   //   let r2 value to enter
                "store r2 r0", // 2
                "add 1 r0",
                "add 1 r2",    // 3
                "compare r0 r1",
                "ble -2",      // 4
                "copy 10 r0",  //   reset array start pointer
                "copy 0 r2",   // 5 let r2 be the array sum
                "copy 0 r3",   //   let r3 be the current value from the array
                "load r1 r3",  // 6
                "add r3 r2",   //
                "add -1 r1",   // 7
                "compare r1 r0",
                "bge -2",      // 8
                "syscall 0"
        };
        Processor p = runProgram(program);
        assertEquals("r2:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,t,f,t,f,f,t,f,", p.output.get(2)); // =210
    }

    @Test
    public void linkedSumTest() {
        String[] program = {
                "copy 10 r0", // 0 address to first node
                "add 10 r0",
                "copy r0 r1", // 1 address to last node
                "copy 1 r2",  //   value to store
                "copy 15 r3", // 2 last value to store
                "add 5 r3",
                // add first item
                "store r2 r1",  // 3
                "add 1 r2",
                // add items
                "copy r1 r4",    // 4 r4=last node's address to the next node
                "add 1 r4",
                "copy r4 r5",    // 5 r5=address for value of the newly added node
                "add 1 r5",      //
                "store r5 r4",   // 6 update previous pointer to next node
                "store r2 r5",   //   store the new value
                "add 1 r2",      // 7
                "copy r5 r1 ",   //
                "compare r2 r3", // 8
                "ble -4",
                // calculate sum
                "copy 0 r6",   // 9  sum value
                "copy r0 r7",  //    r7=pointer to current node
                "load r7 r8",  // 10 load value at current node
                "add r8 r6",   //    add to sum
                "add 1 r7",    // 11 add 1 to pointer for address of next node
                "load r7 r8",  //    set r7 to pointer to next node
                "copy r8 r7",  // 12
                "compare 0 r7",//    loop until next pointer address is 0
                "bne -3",
                "syscall 0"

        };
        Processor p = runProgram(program);
        assertEquals("r6:f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,t,t,f,t,f,f,t,f,", p.output.get(6));
    }

    private static Processor runProgram(String[] program) {
        var assembled = Assembler.assemble(program);
        var merged = Assembler.finalOutput(assembled);
        var m = new Memory();
        m.load(merged);
        var p = new Processor(m);
        p.run();
        return p;
    }
}
