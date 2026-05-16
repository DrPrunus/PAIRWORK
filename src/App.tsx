/**
 * @license
 * SPDX-License-Identifier: Apache-2.0
 */

import { useState } from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { motion, AnimatePresence } from "motion/react";
import Navbar from "./components/Navbar";
import { MOCK_ROOMS, Room } from "./constants";
import { Users, Monitor, Wifi, ShieldCheck } from "lucide-react";

const RoomCard = ({ room }: { room: Room }) => (
  <motion.div
    layout
    initial={{ opacity: 0, y: 20 }}
    animate={{ opacity: 1, y: 0 }}
    className="border border-zinc-800 p-6 bg-zinc-950 hover:bg-zinc-900 transition-all duration-300 group cursor-default"
  >
    <div className="flex justify-between items-start mb-6">
      <span className="text-xs font-mono text-zinc-600">MOD_{room.id.padStart(2, '0')}</span>
      <span className="text-[10px] bg-emerald-900/30 text-emerald-500 px-2 py-1 font-bold tracking-widest uppercase">
        Available
      </span>
    </div>
    
    <div className="mb-6 relative aspect-video overflow-hidden border border-zinc-800">
      <img
        src={room.imageUrl}
        alt={room.name}
        className="w-full h-full object-cover grayscale opacity-50 group-hover:grayscale-0 group-hover:opacity-100 transition-all duration-700 scale-110 group-hover:scale-100"
      />
      <div className="absolute inset-0 bg-gradient-to-t from-zinc-950/80 to-transparent" />
      <div className="absolute bottom-3 left-3 flex items-center gap-2">
        <Users className="w-4 h-4 text-emerald-400" />
        <span className="text-xs font-mono font-bold">{room.capacity}_CAPACITY</span>
      </div>
    </div>

    <h3 className="text-4xl font-black tracking-tight uppercase mb-2 group-hover:text-emerald-400 transition-colors leading-none">
      {room.name.replace(/ /g, '_')}
    </h3>
    <p className="text-zinc-500 text-xs mt-2 font-serif italic mb-6">
      {room.description}
    </p>

    <div className="flex flex-wrap gap-2 mb-8">
      {room.facilities.map((f) => (
        <span key={f} className="text-[10px] uppercase font-mono text-zinc-600 border border-zinc-800 px-2 py-1">
          [{f}]
        </span>
      ))}
    </div>

    <button className="w-full bg-transparent border border-emerald-500/30 text-emerald-400 py-4 hover:bg-emerald-500 hover:text-black transition-all duration-300 font-black uppercase tracking-tighter text-sm">
      INITIATE_BOOKING
    </button>
  </motion.div>
);

const Home = () => {
  return (
    <div className="min-h-screen grid grid-cols-12 gap-px bg-zinc-800">
      {/* Sidebar Info */}
      <section className="col-span-12 lg:col-span-3 bg-zinc-950 p-8 flex flex-col border-b lg:border-b-0 border-zinc-800">
        <h2 className="text-xs text-zinc-500 font-bold uppercase tracking-widest mb-10 border-l-2 border-emerald-500 pl-4">
          01. System_Overview
        </h2>
        
        <div className="mb-12">
          <div className="text-emerald-400 text-xs font-mono mb-2 uppercase">[Campus_Zone]</div>
          <div className="text-6xl font-black tracking-tighter uppercase leading-none">BUILD_A</div>
          <p className="text-zinc-500 text-sm mt-2 italic font-serif">Focus: Technical & Research Hub</p>
        </div>

        <div className="mt-auto space-y-8">
          <div className="p-4 border border-zinc-800 bg-zinc-900 rounded-lg">
            <div className="flex justify-between text-[10px] font-mono text-zinc-500 mb-2 uppercase">
              <span>Usage_Load</span>
              <span>72% AT_CAPACITY</span>
            </div>
            <div className="flex gap-1">
              <div className="h-1 flex-1 bg-emerald-500"></div>
              <div className="h-1 flex-1 bg-emerald-500"></div>
              <div className="h-1 flex-1 bg-emerald-500"></div>
              <div className="h-1 flex-1 bg-emerald-500"></div>
              <div className="h-1 flex-1 bg-zinc-700"></div>
            </div>
          </div>
          
          <div className="flex flex-col gap-2">
            <div className="text-[10px] text-zinc-600 font-mono uppercase tracking-widest">Global_Status</div>
            <div className="flex items-center gap-2">
              <div className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse" />
              <span className="text-xs font-bold uppercase tracking-widest text-zinc-400">Nodes Synced</span>
            </div>
          </div>
        </div>
      </section>

      {/* Main Grid */}
      <section className="col-span-12 lg:col-span-9 bg-zinc-950 p-8">
        <h2 className="text-xs text-zinc-500 font-bold uppercase tracking-widest mb-10 border-l-2 border-white pl-4">
          02. Available_Resources
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-6">
          {MOCK_ROOMS.map((room) => (
            <RoomCard key={room.id} room={room} />
          ))}
        </div>
      </section>
    </div>
  );
};

const MyReservations = () => (
  <div className="min-h-screen bg-zinc-950 p-8">
    <h2 className="text-xs text-zinc-500 font-bold uppercase tracking-widest mb-10 border-l-2 border-emerald-500 pl-4">
      03. Personnel_Records
    </h2>
    <div className="border border-zinc-800 p-12 text-center">
      <div className="text-4xl font-black text-zinc-800 uppercase tracking-tighter mb-4">No_Records_Found</div>
      <p className="font-mono text-xs text-zinc-600 uppercase tracking-widest">Archive is currently empty for local_user</p>
    </div>
  </div>
);

export default function App() {
  return (
    <Router>
      <div className="min-h-screen bg-black font-sans selection:bg-emerald-500 selection:text-black">
        <Navbar />
        <main className="w-full">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/my-reservations" element={<MyReservations />} />
          </Routes>
        </main>
        
        <footer className="h-12 bg-emerald-500 text-black flex items-center justify-between px-8 text-[11px] font-black uppercase tracking-tighter sticky bottom-0 z-50">
          <span>Project_Phase: Alpha_Integration</span>
          <div className="hidden md:flex gap-8">
            <span>Branch: feature/reservation-ui</span>
            <span>Review_Status: Final_Check</span>
            <span>Uptime: 100% stable</span>
          </div>
        </footer>
      </div>
    </Router>
  );
}
