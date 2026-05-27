ALTER TABLE post_likes
ADD COLUMN reviewer_id BIGINT;
ALTER TABLE post_likes
ADD CONSTRAINT fk_post_likes_reviewer
FOREIGN KEY (reviewer_id)
REFERENCES users(id);