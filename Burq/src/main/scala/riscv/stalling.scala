package riscv

import chisel3._
import chisel3.util.Cat
import chisel3.util.Fill
import chisel3.core.SInt

class stalling extends Module{
	val io = IO(new Bundle{


    val memread= Input(UInt(1.W))

    //val exmem_aluout = Input(UInt(32.W))

    val register_rd = Input(UInt(1.W))
    val register_rs1 = Input(UInt(5.W))
    val register_rs2 = Input(UInt(5.W))
    //val memwb_aluout = Input(UInt(32.W))


})




}
