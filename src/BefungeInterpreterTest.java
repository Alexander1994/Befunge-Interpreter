import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by alex on 01/09/18.
 */
class BefungeInterpreterTest {
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
    }

    @org.junit.jupiter.api.Test
    void basic() {
        assertEquals(
                "123456789",
                new BefungeInterpreter().interpret(">987v>.v\nv456<  :\n>321 ^ _@"));
    }

    @org.junit.jupiter.api.Test
    void quine() {
        assertEquals(
                "01->1# +# :# 0# g# ,# :# 5# 8# *# 4# +# -# _@",
                new BefungeInterpreter().interpret("01->1# +# :# 0# g# ,# :# 5# 8# *# 4# +# -# _@"));
    }
}

