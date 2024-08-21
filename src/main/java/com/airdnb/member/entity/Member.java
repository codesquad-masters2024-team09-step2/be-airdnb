package com.airdnb.member.entity;

import com.airdnb.chat.entity.MemberChatRoom;
import com.airdnb.image.entity.Image;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE member SET status = 'DELETED' WHERE id = ?")
@SQLRestriction("status = 'ACTIVE'")
public class Member {

    @Id
    private String id;
    @Setter
    private String password;
    private String name;
    private String role;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "img_id")
    private Image image;
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @OneToMany(mappedBy = "member")
    private List<MemberChatRoom> memberChatRooms = new ArrayList<>();

    @Builder
    public Member(String id, String password, String name, String role, List<MemberChatRoom> memberChatRooms) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.role = role;
        this.memberChatRooms = memberChatRooms;
        this.status = MemberStatus.ACTIVE;
    }

    public boolean hasSameId(String memberId) {
        return id.equals(memberId);
    }

    public enum MemberStatus {
        ACTIVE, DELETED
    }
}
