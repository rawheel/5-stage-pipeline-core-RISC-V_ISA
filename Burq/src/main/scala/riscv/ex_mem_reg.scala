package riscv
import chisel3._

class ex_mem_reg extends Module{
  val io=IO(new Bundle {
    val cont_memwrite = Input(UInt(1.W))
    val cont_memread = Input(UInt(1.W))
    val cont_regwrite = Input(UInt(1.W))
    val alu_branchout = Input(UInt(1.W))
    val alu_out = Input(UInt(32.W))
    val id_ex_rs2in = Input(UInt(32.W))
    val id_ex_rdsel = Input(UInt(5.W))

    val exme_memwrit_out = Output(UInt(1.W))
    val exme_meread_out = Output(UInt(1.W))
    val exme_regwrite_out = Output(UInt(1.W))
    val exme_alubranch_out = Output(UInt(1.W))
    val exme_alu_out = Output(UInt(32.W))
    val exme_rs2_out = Output(UInt(32.W))
    val exme_rdsel_out = Output(UInt(5.W))
  })

  val memwrite_reg = RegInit(0.U(1.W))
  val memread_reg = RegInit(0.U(1.W))
  val regwrite_reg = RegInit(0.U(1.W))
  val branch_reg = RegInit(0.U(1.W))
  val aluout_reg = RegInit(0.U(32.W))
  val rs2out_reg = RegInit(0.U(32.W))
  val rdsel_reg = RegInit(0.U(5.W))

  memwrite_reg := io.cont_memwrite
  memread_reg := io.cont_memread
  regwrite_reg := io.cont_regwrite
  branch_reg := io.alu_branchout
  aluout_reg := io.alu_out
  rs2out_reg := io.id_ex_rs2in
  rdsel_reg := io.id_ex_rdsel

   io.exme_memwrit_out :=  memwrite_reg
   io.exme_meread_out :=  memread_reg
   io.exme_regwrite_out :=   regwrite_reg
   io.exme_alubranch_out :=   branch_reg
   io.exme_alu_out := aluout_reg
   io.exme_rs2_out := rs2out_reg
   io.exme_rdsel_out :=rdsel_reg


}
