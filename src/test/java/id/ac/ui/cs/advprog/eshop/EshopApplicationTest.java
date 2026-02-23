package id.ac.ui.cs.advprog.eshop;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mockStatic;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

class EshopApplicationTest {

    @Test
    void testMainMethod() {
        try (MockedStatic<SpringApplication> mockedSpringApp = mockStatic(SpringApplication.class)) {
            mockedSpringApp.when(() -> SpringApplication.run(EshopApplication.class, new String[]{}))
                    .thenReturn(null);

            EshopApplication.main(new String[]{});

            mockedSpringApp.verify(() -> SpringApplication.run(EshopApplication.class, new String[]{}));
        }
    }
}
