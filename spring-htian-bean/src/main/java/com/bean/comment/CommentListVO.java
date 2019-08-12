package com.bean.comment;

import com.dao.entity.BComment;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CommentListVO {

    private Integer floor;

    private List<BComment> comments = new ArrayList<>();
}
