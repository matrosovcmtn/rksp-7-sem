package ru.matrosov.prac_03.task03;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserFriend {
    private int userId;
    private int friendId;
}
