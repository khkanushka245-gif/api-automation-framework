package com.example.api;

import com.example.base.BaseApiTest;
import com.example.services.UserService;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.emptyOrNullString;

/**
 * Data-driven, order-independent tests for the Users / Posts resources.
 *
 * Stability practices used here:
 *  - Each test is self-contained: it builds its own request via the service and
 *    asserts only on what it controls (no reliance on other tests' state/order).
 *  - Assertions target the contract (status code) and stable fields (id, keys),
 *    never volatile values that the backend may change.
 *  - Timeouts + transient-only retry come from the base fixture / listener.
 */
public class UsersApiTest extends BaseApiTest {

    private UserService users;

    @BeforeClass(alwaysRun = true)
    public void initService() {
        users = new UserService(requestSpec);
    }

    @DataProvider(name = "userIds")
    public Object[][] userIds() {
        return new Object[][] { {1}, {2}, {3} };
    }

    @Test(dataProvider = "userIds")
    public void getUserReturnsExpectedContract(int id) {
        Response resp = users.getUser(id);
        assertThat("status for user " + id, resp.statusCode(), equalTo(200));
        assertThat(resp.jsonPath().getInt("id"), equalTo(id));
        assertThat(resp.jsonPath().getString("email"), not(emptyOrNullString()));
        assertThat(resp.jsonPath().getString("name"), not(emptyOrNullString()));
    }

    @Test
    public void listUsersReturnsNonEmptyCollection() {
        Response resp = users.listUsers();
        assertThat(resp.statusCode(), equalTo(200));
        assertThat(resp.jsonPath().getList("$").size(), greaterThan(0));
    }

    @Test
    public void createPostReturnsCreated() {
        Response resp = users.createPost("demo title", "sample body", 1);
        assertThat(resp.statusCode(), equalTo(201));
        assertThat(resp.jsonPath().getString("title"), equalTo("demo title"));
        // id is server-assigned; assert it exists, not its exact value
        assertThat(resp.jsonPath().get("id"), not(equalTo(null)));
    }
}
