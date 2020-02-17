/**
 * 提交回复
 */
function post() {
  var questionId = $("#question_id").val();
  var content = $("#comment_content").val();

  comment2target(questionId, 1, content);
}

function comment2target(targetId, type, content) {
  if (!content) {
    alert("不能回复空内容！");
    return;
  }
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "/comment",
    data: JSON.stringify({
      "parentId": targetId,
      "content": content,
      "type": type
    }),
    success: function (response) {
      if (response.code == 200) {
        window.location.reload();
      } else {
        if (response.code == 2003) {
          var isAccepted = confirm(response.message);
          if (isAccepted) {
            window.open(
                "https://github.com/login/oauth/authorize?client_id=Iv1.4f61be6432a12e00&redirect_uri=http://localhost:8887/callback&scope=user&state=1");
            window.localStorage.setItem("closable");
          }
        } else {
          alert(response.message);
        }
      }
    },
    dataType: "json"
  });
}

function comment(e) {
  var commentId = e.getAttribute("data-id");
  var content = $("#input-" + commentId).val();
  comment2target(commentId, 2, content);
}

/**
 * 展开二级评论
 */
function collapseComments(e) {
  var id = e.getAttribute("data-id");
  var comments = $("#comment-" + id);

  // 获取一下二级评论展开状态
  var collapse = e.getAttribute("data-collapse");
  if (collapse) {
    // 折叠二级评论
    comments.removeClass("in");
    e.removeAttribute("data-collapse");
    e.classList.remove("active");
  } else {

    $.getJSON("/comment/" + id, function (data) {
      var subCommentContainer = $("#comment-" + id);
      $.each(data.data.reverse(), function (index, comment) {
        var mediaLeftElement = $("<div/>", {
          "class": "media-left"
        }).append($("<img/>", {
          "class": "media-object img-rounded",
          "src": comment.user.avatarUrl
        }));

        var mediaBodyElement = $("<div/>", {
          "class": "media-body"
        }).append($("<h5/>", {
          "class": "media-heading",
          "html": comment.user.name
        })).append($("<div/>", {
          "html": comment.content
        })).append($("<div/>", {
          "class": "menu"
        }).append($("<span/>", {
          "class": "pull-right",
          "html": moment(comment.gmtCreate).format("YYYY-MM-DD")
        })));

        var mediaElement = $("<div/>", {
          "class": "media"
        }).append(mediaLeftElement).append(mediaBodyElement);

        var commentElement = $("<div/>", {
          "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
        }).append(mediaElement);

        subCommentContainer.prepend(commentElement);
      });

      // 展开二级评论
      comments.addClass("in");
      // 标记二级评论展开状态
      e.setAttribute("data-collapse", "in");
      e.classList.add("active");
    });
  }
}

function selectTag(value) {
  var previous = $("#tag").val();
  if (previous.indexOf(value) == -1) {
    if (previous) {
      $("#tag").val(previous + ',' + value);
    } else {
      $("#tag").val(value);
    }
  }
}