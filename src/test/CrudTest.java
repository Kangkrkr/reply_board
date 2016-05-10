import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.pilot.ReplyBoardApplication;
import com.pilot.entity.User;
import com.pilot.repository.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ReplyBoardApplication.class)
@WebAppConfiguration
public class CrudTest {
	@Autowired
	private UserRepository userRepository;
	
	private User user;
	
	@Before
	public void setUp() throws Exception {
		user = new User();
		user.setName("강승윤");
		user.setEmail("kang@kang.com");
		user.setPassword("123123123");
		
		userRepository.save(user);
		userRepository.flush();		// 영속성 컨텍스트에서 플러쉬
	}
	
	@Test
    public void testFind() throws Exception {
        User findUser = userRepository.findOne(user.getId());
        assertEquals(user.getId(), findUser.getId());
    }
}
