export interface Room {
  id: string;
  name: string;
  capacity: number;
  facilities: string[];
  imageUrl: string;
  description: string;
}

export const MOCK_ROOMS: Room[] = [
  {
    id: "1",
    name: "极客研讨室 (Geek Lab)",
    capacity: 6,
    facilities: ["投影仪", "高速Wi-Fi", "人体工学椅"],
    imageUrl: "https://images.unsplash.com/photo-1497366216548-37526070297c?auto=format&fit=crop&q=80&w=800",
    description: "适合小型团队进行技术选型和脑暴，环境极佳。"
  },
  {
    id: "2",
    name: "静谧自习室 (Quiet Zone)",
    capacity: 4,
    facilities: ["降噪墙", "阅读灯"],
    imageUrl: "https://images.unsplash.com/photo-1519389950473-47ba0277781c?auto=format&fit=crop&q=80&w=800",
    description: "绝对安静的环境，适合深度阅读和论文写作。"
  },
  {
    id: "3",
    name: "大型会议厅 (Main Hall)",
    capacity: 20,
    facilities: ["全息投影", "音响系统", "录播设备"],
    imageUrl: "https://images.unsplash.com/photo-1505373633560-fa5a90359e47?auto=format&fit=crop&q=80&w=800",
    description: "承接中型讲座和社团分享活动。"
  }
];
