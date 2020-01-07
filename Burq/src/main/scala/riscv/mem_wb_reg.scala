package riscv
import chisel3._

class mem_wb_reg extends Module{
  val io = (new Bundle{
    val exmem_memwrite_in = Input(UInt(1.W))
    val exmem_memread_in = Input(UInt(1.W))
    val exmem_regwrite_in = Input(UInt(1.W))
    val mainmemout_in = Input(UInt(32.W))
    val exmem_aluot_in = Input(UInt(32.W))
    val exmem_rdsel_in = Input(UInt(5.W))

    val mem_web_memwrite_out = Output(UInt(1.W))
    val mem_web_memread_out = Output(UInt(1.W))
    val mem_web_regwrite_out = Output(UInt(1.W))
    val mem_web_mainmem_out = Output(UInt(32.W))
    val mem_web_aluout_out = Output(UInt(32.W))
    val mem_web_rdsel_out = Output(UInt(5.W))
  })

  val memwrite_reg = RegInit(0.U(1.W))
  val memread_reg = RegInit(0.U(1.W))
  val regwrite_reg = RegInit(0.U(1.W))
  val mainmem_reg = RegInit(0.U(32.W))
  val aluout_reg = RegInit(0.U(32.W))
  val rdsel_reg = RegInit(0.U(5.W))

  memwrite_reg := io.exmem_memwrite_in
  memread_reg := io.exmem_memread_in
  regwrite_reg := io.exmem_regwrite_in
  mainmem_reg :=  io.mainmemout_in
  aluout_reg := io.exmem_aluot_in
  rdsel_reg := io.exmem_rdsel_in


  io.mem_web_memwrite_out := memwrite_reg
  io.mem_web_memread_out := memread_reg
  io.mem_web_regwrite_out := regwrite_reg
  io.mem_web_mainmem_out := mainmem_reg
  io.mem_web_aluout_out := aluout_reg
  io.mem_web_rdsel_out := rdsel_reg


}
