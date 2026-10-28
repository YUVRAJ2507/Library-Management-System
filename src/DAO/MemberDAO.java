package dao;

import java.util.List;
import model.Member;
public interface MemberDAO {

    void addMember(Member member) throws Exception;
    List<Member> getAllMembers() throws Exception;
}
