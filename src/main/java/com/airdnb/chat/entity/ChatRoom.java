package com.airdnb.chat.entity;


import com.airdnb.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "chatroom")
@SQLDelete(sql = "UPDATE chatroom SET status = 'INACTIVE' WHERE id = ?")
@SQLRestriction("status = 'ACTIVE'")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "host_id")
    private Member host;
    @ManyToOne
    @JoinColumn(name = "guest_id")
    private Member guest;
    @OneToMany(mappedBy = "chatRoom")
    private List<ChatMessage> messages = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    ChatStatus status;
    @OneToMany(mappedBy = "chatRoom")
    private List<MemberChatRoom> memberChatRooms = new ArrayList<>();

    @Builder
    public ChatRoom(Member host, Member guest, List<MemberChatRoom> memberChatRooms) {
        this.host = host;
        this.guest = guest;
        this.status = ChatStatus.ACTIVE;
        this.memberChatRooms = memberChatRooms;
    }

    public void softDelete() {
        this.status = ChatStatus.INACTIVE;
    }

    public String getRecipientName(Member member) {
        if (getHost().getId().equals(member.getId())) {
            return getGuest().getName();
        }
        return getHost().getName();
    }
}
