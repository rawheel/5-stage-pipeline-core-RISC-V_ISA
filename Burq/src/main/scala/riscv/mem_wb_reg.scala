package riscv
import chisel3._

class mem_wb_reg extends Module{
  val io = IO(new Bundle{
    val memwb_regwrite_in = Input(UInt(1.W))
    val memtoreg_in = Input(UInt(1.W))

    val memwb_dataout_in = Input(UInt(32.W))
    val memwb_aluout_in = Input(UInt(32.W))
    val memwb_rdsel_in = Input(UInt(5.W))

    val mem_web_regwrite_out = Output(UInt(1.W))
    val mem_web_memtoreg_out = Output(UInt(1.W))
    val mem_web_dataout_out = Output(UInt(32.W))
    val mem_web_aluout_out = Output(UInt(32.W))
    val mem_web_rdsel_out = Output(UInt(5.W))
  })

  val regwrite_reg = RegInit(0.U(1.W))
  val memtoreg_reg = RegInit(0.U(1.W))
  val dataout_reg = RegInit(0.U(32.W))
  val aluout_reg = RegInit(0.U(32.W))
  val rdsel_reg = RegInit(0.U(5.W))

  regwrite_reg := io. memwb_regwrite_in
  memtoreg_reg := io.memtoreg_in
  dataout_reg:=  io.memwb_dataout_in
  aluout_reg := io.memwb_aluout_in
  rdsel_reg := io.memwb_rdsel_in


  io.mem_web_memtoreg_out :=  memtoreg_reg
  io.mem_web_regwrite_out := regwrite_reg
  io.mem_web_dataout_out := dataout_reg
  io.mem_web_aluout_out := aluout_reg
  io.mem_web_rdsel_out := rdsel_reg


}
