package riscv
import chisel3._

class id_ex_reg extends Module{
    val io = IO(new Bundle {

      //come from reg32 rs2 output
      val rs2_in = Input(UInt(32.W)) //str where rs2 output from reg
      val rdsel = Input(UInt(5.W))
      val cont_memwrite = Input(UInt(1.W))
      val cont_memread = Input(UInt(1.W))
      val cont_regwrite = Input(UInt(1.W))
      val cont_memtoreg = Input(UInt(1.W))
      val cont_opA = Input(UInt(32.W))
      val cont_opB = Input(UInt(32.W))
      val alucont_in = Input(UInt(5.W))


      val id_ex_rs2out = Output(UInt(32.W))
      val id_ex_alucont_out = Output(UInt(5.W))
      val id_ex_rdsel_out = Output(UInt(5.W))
      val cont_memwrite_out = Output(UInt(1.W))

      val cont_memread_out = Output(UInt(1.W))
      val cont_regwrite_out = Output(UInt(1.W))
      val cont_memtoreg_out = Output(UInt(1.W))

      val cont_opA_out = Output(UInt(32.W))
      val cont_opB_out = Output(UInt(32.W))
    })

    val id_ex_rs2_reg = RegInit(0.U(32.W))
    val id_ex_memwr_reg = RegInit(0.U(1.W))
    val id_ex_memread_reg = RegInit(0.U(1.W))
    val id_ex_regwrite_reg = RegInit(0.U(1.W))
    val id_ex_memtoreg_reg = RegInit(0.U(1.W))
    val id_ex_opA_reg = RegInit(0.U(32.W))
    val id_ex_opB_reg = RegInit(0.U(32.W))
    val id_ex_alucont_reg = RegInit(0.U(5.W))
    val id_ex_rd_reg = RegInit(0.U(5.W))




    id_ex_rs2_reg := io.rs2_in
    id_ex_rd_reg := io.rdsel
    id_ex_memwr_reg := io.cont_memwrite
    id_ex_memread_reg := io.cont_memread
    id_ex_regwrite_reg :=io.cont_regwrite
    id_ex_memtoreg_reg := io.cont_memtoreg
    id_ex_opA_reg :=  io.cont_opA
    id_ex_opB_reg := io.cont_opB
    id_ex_alucont_reg := io.alucont_in

    io.id_ex_rs2out := id_ex_rs2_reg
    io.id_ex_alucont_out := id_ex_alucont_reg
    io.cont_memwrite_out := id_ex_memwr_reg
    io.cont_memread_out :=id_ex_memread_reg
    io.cont_regwrite_out :=id_ex_regwrite_reg
    io.cont_memtoreg_out :=   id_ex_memtoreg_reg
    io.cont_opA_out :=   id_ex_opA_reg
    io.cont_opB_out :=id_ex_opB_reg
    io.id_ex_rdsel_out := id_ex_rd_reg

}
