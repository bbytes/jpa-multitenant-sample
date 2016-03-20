package com.bbytes.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bbytes.JpaMultitenantApplication;
import com.bbytes.config.jpa.multitenant.dsrouting.TenantContextHolder;
import com.bbytes.domain.Organization;
import com.bbytes.domain.User;
import com.bbytes.domain.UserRole;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { JpaMultitenantApplication.class })
public class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private OrganizationRepository organizationRepository;

	private Organization org = new Organization("test", "test");
	
	private String tenantName = "dummy";

	@Before
	public void init() {
		TenantContextHolder.setTenantName(tenantName);
		userRoleRepository.save(UserRole.ADMIN_USER_ROLE);
		userRoleRepository.save(UserRole.NORMAL_USER_ROLE);
		organizationRepository.save(org);

	}
	
	@After
	public void cleanUp() {
		TenantContextHolder.setTenantName(tenantName);
		userRepository.deleteAll();
		userRoleRepository.deleteAll();
		organizationRepository.deleteAll();

	}

	@Test
	public void testSaveUser() {
		// setup user
		User user = new User("test", "sample@sss.com");
		user.setOrganization(org);

		// save user, verify has ID value after save
		assertNull(user.getUserId()); // null before save
		userRepository.save(user);
		assertNotNull(user.getUserId()); // not null after save

		// fetch from DB
		User fetchedUser = userRepository.findOne(user.getUserId());

		// should not be null
		assertNotNull(fetchedUser);

		// should equal
		assertEquals(user.getUserId(), fetchedUser.getUserId());

		// update description and save
		user.setName("New name");
		userRepository.save(user);

		// get from DB, should be updated
		User fetchedUpdateduser = userRepository.findOne(fetchedUser.getUserId());
		assertEquals(user.getName(), fetchedUpdateduser.getName());

		// verify count of users in DB
		long userCount = userRepository.count();
		assertEquals(userCount, 1);

		// get all user, list should only have one
		Iterable<User> users = userRepository.findAll();

		int count = 0;

		for (User u : users) {
			count++;
		}

		assertEquals(count, 1);
	}
}