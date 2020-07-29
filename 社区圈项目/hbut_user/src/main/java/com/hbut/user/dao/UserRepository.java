package com.hbut.user.dao;

import com.hbut.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findUserById(Integer id);
    User findUserByMobile(String mobile);
    User findUserByEmail(String email);


    @Query(value = "SELECT * FROM user,friends WHERE id = friend_id AND user_id = ?1 AND friend_status = ?2",nativeQuery = true)
    List<User> findFriends(Integer id,Integer friendStatus);

    @Modifying
    @Query(value = "INSERT INTO friends(user_id,friend_id,friend_status,createTime) VALUES (?1,?2,?3,?4)",nativeQuery = true)
    int insertFriend(Integer userId, Integer friendId, Integer friendStatus, Date createTime);

    @Modifying
    @Query(value = "UPDATE friends SET friend_status = ?3 WHERE user_id = ?1 AND friend_id = ?2",nativeQuery = true)
    int updateFriend(Integer userId,Integer friendId,Integer friendStatus);

    @Modifying
    @Query(value = "DELETE FROM friends WHERE user_id = ?1 AND friend_id = ?2",nativeQuery = true)
    int deleteFriend(Integer userId,Integer friendId);

    @Query(value = "SELECT * FROM user,friends WHERE id = friend_id AND user_id = ?1 AND friend_id = ?2",nativeQuery = true)
    User findFriend(Integer userId,Integer friendId);
}
