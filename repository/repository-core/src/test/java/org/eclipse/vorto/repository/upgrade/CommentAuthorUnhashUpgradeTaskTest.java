package org.eclipse.vorto.repository.upgrade;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.eclipse.vorto.model.ModelId;
import org.eclipse.vorto.repository.account.User;
import org.eclipse.vorto.repository.comment.Comment;
import org.eclipse.vorto.repository.comment.ICommentService;
import org.eclipse.vorto.repository.core.impl.UserContext;
import org.eclipse.vorto.repository.upgrade.impl.CommentAuthorUnhashUpgradeTask;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class CommentAuthorUnhashUpgradeTaskTest {

  public ICommentService commentService = Mockito.mock(ICommentService.class);

  private OAuth2Authentication mockUser = Mockito.mock(OAuth2Authentication.class);

  private UsernamePasswordAuthenticationToken mockedAuthToken =
      Mockito.mock(UsernamePasswordAuthenticationToken.class);

  private UserContext userContext = UserContext.user("erleczars.mantos");

  private UserContext userContext2 = UserContext.user("emantos");

  private List<Comment> commentsForEmantos = getComments(userContext2.getHashedUsername());
  private List<Comment> commentsForEmailPrefix = getComments(userContext.getHashedUsername());

  @Before
  public void init() {
    Mockito.when(mockedAuthToken.getDetails()).thenReturn(getDetails());
    Mockito.when(mockUser.getUserAuthentication()).thenReturn(mockedAuthToken);

    Mockito.when(commentService.getCommentsByAuthor(userContext.getHashedUsername()))
        .thenReturn(commentsForEmailPrefix);

    Mockito.when(commentService.getCommentsByAuthor(userContext2.getHashedUsername()))
        .thenReturn(commentsForEmantos);
  }

  private List<Comment> getComments(String author) {
    return IntStream.range(0, 10).mapToObj((i) -> {
      ModelId id = new ModelId("Erle" + i, "com.erle", "1.0.0");
      Comment comment = new Comment(id, author, author + " comment no. " + i);
      return comment;
    }).collect(Collectors.toList());
  }

  private Map<String, Object> getDetails() {
    Map<String, Object> details = new HashMap<String, Object>();
    details.put("email", "erleczars.mantos@bosch-si.com");
    return details;
  }

  @Test
  public void testUserUpdateTask() {
    CommentAuthorUnhashUpgradeTask updateTask = new CommentAuthorUnhashUpgradeTask();
    updateTask.setCommentService(commentService);

    commentsForEmantos.forEach(
        comment -> assertTrue(comment.getAuthor().equals(userContext2.getHashedUsername())));
    commentsForEmailPrefix.forEach(
        comment -> assertTrue(comment.getAuthor().equals(userContext.getHashedUsername())));

    updateTask.doUpgrade(User.create("emantos"), () -> mockUser);

    commentsForEmantos.forEach(comment -> assertTrue(comment.getAuthor().equals("emantos")));
    commentsForEmailPrefix.forEach(comment -> assertTrue(comment.getAuthor().equals("emantos")));

    ArgumentCaptor<Comment> argument = ArgumentCaptor.forClass(Comment.class);
    Mockito.verify(commentService, Mockito.times(20)).saveComment(argument.capture());

    assertEquals("emantos", argument.getValue().getAuthor());
  }

}
