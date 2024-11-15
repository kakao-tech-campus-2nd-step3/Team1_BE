package team1.be.seamless.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team1.be.seamless.dto.TestDTO;
import team1.be.seamless.dto.TestDTO.create;
import team1.be.seamless.entity.TestEntity;
import team1.be.seamless.mapper.UserMapper;
import team1.be.seamless.repository.TestRepository;
import team1.be.seamless.util.auth.JwtToken;

@ExtendWith(MockitoExtension.class)
class TestServiceTest {

    @InjectMocks
    private TestService testService;

    @Mock
    private TestRepository testRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private JwtToken jwtToken;

    private final long id = 1L;
    private String name = "testUser";

    private TestEntity mockTest;

    @BeforeEach
    public void setUp() {
        mockTest = new TestEntity(name);

        lenient().when(testRepository.findById(id)).thenReturn(Optional.of(mockTest));
        lenient().when(testRepository.save(mockTest)).thenReturn(mockTest);
    }

    @Test
    void 테스트함수_조회_테스트() {
        TestEntity test = testService.getTest(id);

        assertEquals(name, test.getName());
    }

    @Test
    void 테스트함수_생성_테스트() {

        TestDTO.create create = new create(name);

        TestEntity test = testService.createTest(create);

        assertEquals(name, test.getName());
    }
}
