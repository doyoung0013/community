ALTER TABLE posts DROP FOREIGN KEY fk_posts_category;
ALTER TABLE posts
ADD CONSTRAINT fk_posts_category_to_users
FOREIGN KEY (category_id)
REFERENCES users(id);