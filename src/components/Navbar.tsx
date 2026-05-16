import { Link } from "react-router-dom";
import { BookOpen, Calendar, User, LogIn, Monitor } from "lucide-react";

export default function Navbar() {
  return (
    <header className="border-b border-zinc-800 p-8 flex justify-between items-end bg-zinc-950 sticky top-0 z-50">
      <div>
        <Link to="/" className="group">
          <h1 className="text-6xl md:text-8xl font-black tracking-tighter leading-none uppercase group-hover:text-emerald-400 transition-colors">
            Study_Space
          </h1>
        </Link>
        <p className="text-zinc-500 font-mono mt-2 uppercase tracking-widest text-[10px]">
          Terminal ID: SYS-RESERVE-042 // Campus Activity Core
        </p>
      </div>
      
      <nav className="flex flex-col items-end gap-4">
        <div className="flex items-center gap-6 mb-2">
          <Link
            to="/"
            className="text-zinc-400 hover:text-white uppercase font-bold tracking-widest text-xs flex items-center gap-2 transition-colors"
          >
            <Calendar className="w-4 h-4 text-emerald-500" />
            <span>Reservations</span>
          </Link>
          <Link
            to="/my-reservations"
            className="text-zinc-400 hover:text-white uppercase font-bold tracking-widest text-xs flex items-center gap-2 transition-colors"
          >
            <User className="w-4 h-4 text-emerald-500" />
            <span>Profile</span>
          </Link>
        </div>
        <div className="text-right">
          <div className="text-4xl font-mono text-emerald-400 font-bold tracking-tighter italic">
            ACTIVE
          </div>
          <div className="text-[10px] text-zinc-500 uppercase tracking-widest font-bold">
            System Status
          </div>
        </div>
      </nav>
    </header>
  );
}
