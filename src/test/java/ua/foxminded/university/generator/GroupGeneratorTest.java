package ua.foxminded.university.generator;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import ua.foxminded.university.data.Group;

class GroupGeneratorTest {
    private static final String GROUP_NAME = "NL-77";
    private GroupGenerator groupGenerator;

    @BeforeEach
    void setUp() {
        groupGenerator = Mockito.mock(GroupGenerator.class);
    }

    @Test
    void shouldExecuteOnceWhenGroupGeneratorIsVerified() {
        Group expectedGroup = new Group(GROUP_NAME);
        when(groupGenerator.getGroup()).thenReturn(expectedGroup);
        groupGenerator.getGroup();
        verify(groupGenerator, times(1)).getGroup();
    }
}
