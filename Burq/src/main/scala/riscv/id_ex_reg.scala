package riscv
import chisel3._

class id_ex_reg extends Module{
    val io = IO(new Bundle {
      val if_id_pcout = Input(UInt(32.W))
      val rs1_out1 = Input(UInt(32.W))
      val rs2_out2 = Input(UInt(32.W))
      val imm_out = Input(UInt(32.W))
      val ins_out = Input(UInt(32.W))
      val rdsel = Input(UInt(5.W))
      val cont_memwrite = Input(UInt(1.W))
      val cont_branch = Input(UInt(1.W))
      val cont_memread = Input(UInt(1.W))
      val cont_regwrite = Input(UInt(1.W))
      val cont_memtoreg = Input(UInt(1.W))
      val cont_aluop = Input(UInt(3.W))
      val cont_opA = Input(UInt(2.W))
      val cont_opB = Input(UInt(1.W))
      val cont_nextpcsel = Input(UInt(2.W))

      val id_ex_pcout = Output(UInt(32.W))
      val id_ex_rs1out = Output(UInt(32.W))
      val id_ex_rs2out = Output(UInt(32.W))
      val id_ex_immout = Output(UInt(32.W))
      val id_ex_insout = Output(UInt(32.W))
      val id_ex_rdsel_out = Output(UInt(32.W))
      val cont_memwrite_out = Output(UInt(1.W))
      val cont_branch_out = Output(UInt(1.W))
      val cont_memread_out = Output(UInt(1.W))
      val cont_regwrite_out = Output(UInt(1.W))
      val cont_memtoreg_out = Output(UInt(1.W))
      val cont_aluop_out = Output(UInt(3.W))
      val cont_opA_out = Output(UInt(2.W))
      val cont_opB_out = Output(UInt(1.W))
      val cont_nextpcsel_out = Output(UInt(2.W))
    })
    val id_ex_pc_reg = RegInit(0.U(32.W))
    val id_ex_rs1_reg = RegInit(0.U(32.W))
    val id_ex_rs2_reg = RegInit(0.U(32.W))
    val id_ex_imm_reg = RegInit(0.U(32.W))
    val id_ex_ins_reg = RegInit(0.U(32.W))
    val id_ex_rdsel_reg = RegInit(0.U(5.W))
    val id_ex_memwr_reg = RegInit(0.U(1.W))
    val id_ex_branch_reg = RegInit(0.U(1.W))
    val id_ex_memread_reg = RegInit(0.U(1.W))
    val id_ex_regwrite_reg = RegInit(0.U(1.W))
    val id_ex_memtoreg_reg = RegInit(0.U(1.W))
    val id_ex_aluop_reg = RegInit(0.U(3.W))
    val id_ex_opA_reg = RegInit(0.U(2.W))
    val id_ex_opB_reg = RegInit(0.U(1.W))
    val id_ex_nextpcsel_reg = RegInit(0.U(2.W))

    id_ex_pc_reg := io.if_id_pcout
    id_ex_rs1_reg := io.rs1_out1
    id_ex_rs2_reg := io.rs2_out2
    id_ex_imm_reg := io.imm_out
    id_ex_ins_reg := io.ins_out
    id_ex_rdsel_reg := io.rdsel
    id_ex_memwr_reg := io.cont_memwrite
    id_ex_branch_reg :=io.cont_branch
    id_ex_memread_reg := io.cont_memread
    id_ex_regwrite_reg :=io.cont_regwrite
    id_ex_memtoreg_reg := io.cont_memtoreg
    id_ex_aluop_reg :=  io.cont_aluop
    id_ex_opA_reg :=  io.cont_opA
    id_ex_opB_reg := io.cont_opB
    id_ex_nextpcsel_reg := io.cont_nextpcsel

    io.id_ex_pcout := id_ex_pc_reg
    io.id_ex_rs1out := id_ex_rs1_reg
    io.id_ex_rs2out := id_ex_rs2_reg
    io.id_ex_immout := id_ex_imm_reg
    io.id_ex_insout := id_ex_ins_reg
    io.id_ex_rdsel_out := id_ex_rdsel_reg
    io.cont_memwrite_out := id_ex_memwr_reg
    io.cont_branch_out :=   id_ex_branch_reg
    io.cont_memread_out :=id_ex_memread_reg
    io.cont_regwrite_out :=id_ex_regwrite_reg
    io.cont_memtoreg_out :=   id_ex_memtoreg_reg
    io.cont_aluop_out := id_ex_aluop_reg
    io.cont_opA_out :=   id_ex_opA_reg
    io.cont_opB_out :=id_ex_opB_reg
    io.cont_nextpcsel_out := id_ex_nextpcsel_reg

}
