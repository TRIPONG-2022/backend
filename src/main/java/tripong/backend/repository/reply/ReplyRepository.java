package tripong.backend.repository.reply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tripong.backend.entity.reply.Reply;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {


}
