package dao;

import java.util.ArrayList;
import model.Member;

public interface MemberDAO {
    void addMember(Member member);
    ArrayList<Member> getAllMembers();
}